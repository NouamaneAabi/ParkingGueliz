package com.parkingmanager.service;

import com.parkingmanager.dao.AbonneDAO;
import com.parkingmanager.dao.VehiculeDAO;
import com.parkingmanager.model.Abonne;
import com.parkingmanager.model.Vehicule;

import java.time.LocalDate;

public class AbonnementService {
    private AbonneDAO abonneDAO;
    private VehiculeDAO vehiculeDAO;

    public AbonnementService() {
        this.abonneDAO = new AbonneDAO();
        this.vehiculeDAO = new VehiculeDAO();
    }

    /**
     * Vérifie si un véhicule a un abonnement actif
     */
    public boolean estAbonneActif(String immatriculation) {
        Vehicule vehicule = vehiculeDAO.findByImmatriculation(immatriculation);
        if (vehicule == null) {
            return false;
        }
        
        Abonne abonne = abonneDAO.findByVehiculeId(vehicule.getId());
        return abonne != null && abonne.estActif();
    }

    /**
     * Vérifie si un véhicule a un abonnement actif
     */
    public boolean estAbonneActif(Vehicule vehicule) {
        if (vehicule == null) {
            return false;
        }
        Abonne abonne = abonneDAO.findByVehiculeId(vehicule.getId());
        return abonne != null && abonne.estActif();
    }

    /**
     * Crée un nouvel abonnement
     */
    public void creerAbonnement(String immatriculation, LocalDate dateDebut, LocalDate dateFin) {
        Vehicule vehicule = vehiculeDAO.findByImmatriculation(immatriculation);
        if (vehicule == null) {
            throw new IllegalArgumentException("Véhicule non trouvé: " + immatriculation);
        }

        // Vérifier si un abonnement existe déjà
        Abonne abonneExistant = abonneDAO.findByVehiculeId(vehicule.getId());
        if (abonneExistant != null) {
            throw new IllegalArgumentException("Un abonnement existe déjà pour ce véhicule");
        }

        Abonne abonne = new Abonne(vehicule, dateDebut, dateFin);
        abonneDAO.save(abonne);
    }

    /**
     * Renouvelle un abonnement
     */
    public void renouvelerAbonnement(String immatriculation, LocalDate nouvelleDateFin) {
        Vehicule vehicule = vehiculeDAO.findByImmatriculation(immatriculation);
        if (vehicule == null) {
            throw new IllegalArgumentException("Véhicule non trouvé: " + immatriculation);
        }

        Abonne abonne = abonneDAO.findByVehiculeId(vehicule.getId());
        if (abonne == null) {
            throw new IllegalArgumentException("Aucun abonnement trouvé pour ce véhicule");
        }

        abonne.setDateFin(nouvelleDateFin);
        abonneDAO.update(abonne);
    }
}





