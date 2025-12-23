package com.parkingmanager.service;

import com.parkingmanager.model.Ticket;
import com.parkingmanager.dao.ConfigurationDAO;
import java.time.Duration;
import java.time.LocalDateTime;

public class CalculTarifService {
    private ConfigurationDAO configDAO;

    public CalculTarifService() {
        this.configDAO = new ConfigurationDAO();
    }

    public double getTarifHeure() {
        String val = configDAO.getValue("tarif_heure", "2.0");
        return Double.parseDouble(val);
    }

    public double getTarifMinimum() {
        String val = configDAO.getValue("tarif_minimum", "1.0");
        return Double.parseDouble(val);
    }

    private double getTarifMinute() {
        return getTarifHeure() / 60.0;
    }

    /**
     * Calcule le montant à payer pour un ticket
     * @param ticket Le ticket avec entree et sortie
     * @return Le montant calculé
     */
    public double calculerMontant(Ticket ticket) {
        if (ticket.getSortie() == null) {
            return 0.0; // Ticket non clôturé
        }

        LocalDateTime entree = ticket.getEntree();
        LocalDateTime sortie = ticket.getSortie();
        
        Duration duree = Duration.between(entree, sortie);
        long minutes = duree.toMinutes();
        
        // Si moins d'une minute, on compte 1 minute
        if (minutes < 1) {
            minutes = 1;
        }
        
        double montant = minutes * getTarifMinute();
        
        // Application du tarif minimum
        if (montant < getTarifMinimum()) {
            montant = getTarifMinimum();
        }
        
        return Math.round(montant * 100.0) / 100.0; // Arrondi à 2 décimales
    }

    /**
     * Calcule le montant pour une durée donnée
     * @param dureeMinutes Durée en minutes
     * @return Le montant calculé
     */
    public double calculerMontant(long dureeMinutes) {
        if (dureeMinutes < 1) {
            dureeMinutes = 1;
        }
        
        double montant = dureeMinutes * getTarifMinute();
        
        if (montant < getTarifMinimum()) {
            montant = getTarifMinimum();
        }
        
        return Math.round(montant * 100.0) / 100.0;
    }
}
