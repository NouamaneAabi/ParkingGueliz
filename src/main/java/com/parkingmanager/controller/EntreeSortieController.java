package com.parkingmanager.controller;

import com.parkingmanager.model.Place;
import com.parkingmanager.model.Paiement;
import com.parkingmanager.service.ParkingService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class EntreeSortieController {
    @FXML
    private TextField immatriculationEntreeField;
    @FXML
    private TextField proprietaireField;
    @FXML
    private ComboBox<Integer> placeComboBox;
    @FXML
    private TextField immatriculationSortieField;
    @FXML
    private Label messageLabel;
    @FXML
    private Label montantLabel;

    private ParkingService parkingService;
    private DashboardController dashboardController;

    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

    @FXML
    public void initialize() {
        parkingService = new ParkingService();
        chargerPlacesLibres();
    }

    private void chargerPlacesLibres() {
        List<Place> placesLibres = parkingService.getPlacesLibres();
        ObservableList<Integer> numerosPlaces = FXCollections.observableArrayList();
        for (Place place : placesLibres) {
            numerosPlaces.add(place.getNumero());
        }
        placeComboBox.setItems(numerosPlaces);
    }

    @FXML
    private void enregistrerEntree() {
        try {
            String immatriculation = immatriculationEntreeField.getText().trim();
            String proprietaire = proprietaireField.getText().trim();
            Integer numeroPlace = placeComboBox.getValue();

            if (immatriculation.isEmpty() || proprietaire.isEmpty() || numeroPlace == null) {
                afficherMessage("Veuillez remplir tous les champs", true);
                return;
            }

            parkingService.enregistrerEntree(immatriculation, proprietaire, numeroPlace);
            afficherMessage("Entrée enregistrée avec succès!", false);
            reinitialiserFormulaireEntree();
            chargerPlacesLibres();
            
            // Rafraîchir le dashboard après 200ms pour assurer que la DB est à jour
            if (dashboardController != null) {
                new Thread(() -> {
                    try {
                        Thread.sleep(200);
                        System.out.println("🔄 Appel refreshData() du dashboard après entrée...");
                        dashboardController.refreshData();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }).start();
            } else {
                System.err.println("⚠️ dashboardController est NULL, impossible de rafraîchir");
            }
        } catch (Exception e) {
            afficherMessage("Erreur: " + e.getMessage(), true);
        }
    }

    @FXML
    private void enregistrerSortie() {
        try {
            String immatriculation = immatriculationSortieField.getText().trim();

            if (immatriculation.isEmpty()) {
                afficherMessage("Veuillez saisir l'immatriculation", true);
                return;
            }

            Paiement paiement = parkingService.enregistrerSortie(immatriculation);
            
            if (paiement != null) {
                montantLabel.setText(String.format("Montant à payer: %.2f €", paiement.getMontant()));
                afficherMessage("Sortie enregistrée avec succès!", false);
            } else {
                montantLabel.setText("Abonné actif - Aucun paiement requis");
                afficherMessage("Sortie enregistrée (abonné actif)", false);
            }
            
            immatriculationSortieField.clear();
            chargerPlacesLibres();
            
            // Rafraîchir le dashboard après 200ms pour assurer que la DB est à jour
            if (dashboardController != null) {
                new Thread(() -> {
                    try {
                        Thread.sleep(200);
                        System.out.println("🔄 Appel refreshData() du dashboard après sortie...");
                        dashboardController.refreshData();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }).start();
            } else {
                System.err.println("⚠️ dashboardController est NULL, impossible de rafraîchir");
            }
        } catch (Exception e) {
            afficherMessage("Erreur: " + e.getMessage(), true);
            montantLabel.setText("");
        }
    }

    private void reinitialiserFormulaireEntree() {
        immatriculationEntreeField.clear();
        proprietaireField.clear();
        placeComboBox.setValue(null);
    }

    private void afficherMessage(String message, boolean erreur) {
        messageLabel.setText(message);
        messageLabel.setStyle(erreur ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
    }
}

