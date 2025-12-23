package com.parkingmanager.service;

import com.parkingmanager.dao.*;
import com.parkingmanager.model.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ParkingService {
    private PlaceDAO placeDAO;
    private VehiculeDAO vehiculeDAO;
    private TicketDAO ticketDAO;
    private PaiementDAO paiementDAO;
    private CalculTarifService calculTarifService;
    private AbonnementService abonnementService;

    // Pattern pour valider l'immatriculation française (format: AB-123-CD)
    private static final Pattern IMMATRICULATION_PATTERN = 
        Pattern.compile("^[A-Z]{2}-\\d{3}-[A-Z]{2}$");

    public ParkingService() {
        this.placeDAO = new PlaceDAO();
        this.vehiculeDAO = new VehiculeDAO();
        this.ticketDAO = new TicketDAO();
        this.paiementDAO = new PaiementDAO();
        this.calculTarifService = new CalculTarifService();
        this.abonnementService = new AbonnementService();
    }

    /**
     * Valide le format d'une immatriculation
     */
    public boolean validerImmatriculation(String immatriculation) {
        if (immatriculation == null || immatriculation.trim().isEmpty()) {
            return false;
        }
        return IMMATRICULATION_PATTERN.matcher(immatriculation.trim().toUpperCase()).matches();
    }

    /**
     * Enregistre l'entrée d'un véhicule
     */
    public Ticket enregistrerEntree(String immatriculation, String proprietaire, Integer numeroPlace) {
        // Validation de l'immatriculation
        if (!validerImmatriculation(immatriculation)) {
            throw new IllegalArgumentException("Format d'immatriculation invalide. Format attendu: AB-123-CD");
        }

        immatriculation = immatriculation.trim().toUpperCase();

        // Vérifier si la place existe et est libre
        Place place;
        try {
            place = placeDAO.findByNumero(numeroPlace);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la recherche de la place", e);
        }
        if (place == null) {
            throw new IllegalArgumentException("Place non trouvée: " + numeroPlace);
        }
        if ("OCCUPEE".equals(place.getStatut())) {
            throw new IllegalArgumentException("La place " + numeroPlace + " est déjà occupée");
        }

        // Vérifier si le véhicule a déjà un ticket ouvert
        Vehicule vehicule = vehiculeDAO.findByImmatriculation(immatriculation);
        if (vehicule == null) {
            vehicule = new Vehicule(immatriculation, proprietaire);
            vehiculeDAO.save(vehicule);
        } else {
            Ticket ticketOuvert = ticketDAO.findTicketOuvertByVehicule(vehicule.getId());
            if (ticketOuvert != null) {
                throw new IllegalArgumentException("Ce véhicule a déjà un ticket ouvert");
            }
        }

        // Créer le ticket
        Ticket ticket = new Ticket(place, vehicule);
        ticketDAO.save(ticket);

        // Marquer la place comme occupée
        try {
            System.out.println("🔄 Mise à jour du statut de la place " + numeroPlace + " à OCCUPEE");
            placeDAO.updateStatut(numeroPlace, "OCCUPEE");
            System.out.println("✅ Place " + numeroPlace + " mise à jour avec succès en OCCUPEE");
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la mise à jour de la place : " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la mise à jour de la place", e);
        }

        return ticket;
    }

    /**
     * Enregistre la sortie d'un véhicule
     */
    public Paiement enregistrerSortie(String immatriculation) {
        immatriculation = immatriculation.trim().toUpperCase();

        Vehicule vehicule = vehiculeDAO.findByImmatriculation(immatriculation);
        if (vehicule == null) {
            throw new IllegalArgumentException("Véhicule non trouvé: " + immatriculation);
        }

        Ticket ticket = ticketDAO.findTicketOuvertByVehicule(vehicule.getId());
        if (ticket == null) {
            throw new IllegalArgumentException("Aucun ticket ouvert trouvé pour ce véhicule");
        }

        // Clôturer le ticket
        ticket.setSortie(LocalDateTime.now());
        
        // Calculer le montant (gratuit pour les abonnés actifs)
        double montant = 0.0;
        if (!abonnementService.estAbonneActif(vehicule)) {
            montant = calculTarifService.calculerMontant(ticket);
        }
        ticket.setMontant(montant);
        ticketDAO.update(ticket);

        // Libérer la place
        Place place = ticket.getPlace();
        try {
            System.out.println("🔄 Mise à jour du statut de la place " + place.getNumero() + " à LIBRE");
            placeDAO.updateStatut(place.getNumero(), "LIBRE");
            System.out.println("✅ Place " + place.getNumero() + " mise à jour avec succès en LIBRE");
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la libération de la place : " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la libération de la place", e);
        }

        // Créer le paiement si nécessaire
        Paiement paiement = null;
        if (montant > 0) {
            paiement = new Paiement(ticket, montant);
            paiementDAO.save(paiement);
        }

        return paiement;
    }

    /**
     * Récupère toutes les places
     */
    public List<Place> getToutesLesPlaces() {
        try {
            return placeDAO.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération des places", e);
        }
    }

    /**
     * Récupère les places libres
     */
    public List<Place> getPlacesLibres() {
        try {
            return placeDAO.findAll().stream()
                    .filter(p -> "LIBRE".equals(p.getStatut()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération des places libres", e);
        }
    }

    /**
     * Récupère les places occupées
     */
    public List<Place> getPlacesOccupees() {
        try {
            return placeDAO.findAll().stream()
                    .filter(p -> "OCCUPEE".equals(p.getStatut()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération des places occupées", e);
        }
    }

    /**
     * Calcule le taux d'occupation
     */
    public double calculerTauxOccupation() {
        try {
            int libres = placeDAO.countPlacesLibres();
            int occupees = placeDAO.countPlacesOccupees();
            long total = libres + occupees;
            if (total == 0) {
                return 0.0;
            }
            return (occupees * 100.0) / total;
        } catch (Exception e) {
            System.err.println("Erreur lors du calcul du taux d'occupation: " + e.getMessage());
            return 0.0;
        }
    }

    /**
     * Récupère l'historique des tickets
     */
    public List<Ticket> getHistoriqueTickets() {
        return ticketDAO.findAll();
    }

    /**
     * Récupère les recettes du jour
     */
    public double getRecettesDuJour() {
        return paiementDAO.getRecettesDuJour(java.time.LocalDate.now());
    }
}





