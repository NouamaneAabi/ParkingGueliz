package com.parkingmanager.controller;

import com.parkingmanager.dao.AbonneDAO;
import com.parkingmanager.dao.VehiculeDAO;
import com.parkingmanager.model.Abonne;
import com.parkingmanager.model.Vehicule;
import com.parkingmanager.service.AbonnementService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;

public class AbonnesController {
    @FXML
    private TextField immatriculationField;
    @FXML
    private TextField proprietaireField;
    @FXML
    private DatePicker dateDebutPicker;
    @FXML
    private DatePicker dateFinPicker;
    @FXML
    private TableView<Abonne> abonnesTable;
    @FXML
    private TableColumn<Abonne, String> immatriculationColumn;
    @FXML
    private TableColumn<Abonne, String> proprietaireColumn;
    @FXML
    private TableColumn<Abonne, LocalDate> dateDebutColumn;
    @FXML
    private TableColumn<Abonne, LocalDate> dateFinColumn;
    @FXML
    private TableColumn<Abonne, String> statutColumn;
    @FXML
    private Label messageLabel;

    private AbonnementService abonnementService;
    private AbonneDAO abonneDAO;
    private VehiculeDAO vehiculeDAO;

    @FXML
    public void initialize() {
        abonnementService = new AbonnementService();
        abonneDAO = new AbonneDAO();
        vehiculeDAO = new VehiculeDAO();
        setupTable();
        chargerAbonnes();
    }

    private void setupTable() {
        immatriculationColumn.setCellValueFactory(cellData -> {
            Vehicule v = cellData.getValue().getVehicule();
            return new javafx.beans.property.SimpleStringProperty(v != null ? v.getImmatriculation() : "");
        });
        proprietaireColumn.setCellValueFactory(cellData -> {
            Vehicule v = cellData.getValue().getVehicule();
            return new javafx.beans.property.SimpleStringProperty(v != null ? v.getProprietaire() : "");
        });
        dateDebutColumn.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        dateFinColumn.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        statutColumn.setCellValueFactory(cellData -> {
            Abonne abonne = cellData.getValue();
            String statut = abonne.estActif() ? "Actif" : "Expiré";
            return new javafx.beans.property.SimpleStringProperty(statut);
        });
    }

    @FXML
    private void creerAbonnement() {
        try {
            String immatriculation = immatriculationField.getText().trim();
            String proprietaire = proprietaireField.getText().trim();
            LocalDate dateDebut = dateDebutPicker.getValue();
            LocalDate dateFin = dateFinPicker.getValue();

            if (immatriculation.isEmpty() || proprietaire.isEmpty() || dateDebut == null || dateFin == null) {
                afficherMessage("Veuillez remplir tous les champs", true);
                return;
            }

            if (dateFin.isBefore(dateDebut)) {
                afficherMessage("La date de fin doit être après la date de début", true);
                return;
            }

            // Créer le véhicule s'il n'existe pas
            Vehicule vehicule = vehiculeDAO.findByImmatriculation(immatriculation);
            if (vehicule == null) {
                vehicule = new Vehicule(immatriculation, proprietaire);
                vehiculeDAO.save(vehicule);
            }

            abonnementService.creerAbonnement(immatriculation, dateDebut, dateFin);
            afficherMessage("Abonnement créé avec succès!", false);
            reinitialiserFormulaire();
            chargerAbonnes();
        } catch (Exception e) {
            afficherMessage("Erreur: " + e.getMessage(), true);
        }
    }

    @FXML
    private void chargerAbonnes() {
        List<Abonne> abonnes = abonneDAO.findAll();
        ObservableList<Abonne> abonnesList = FXCollections.observableArrayList(abonnes);
        abonnesTable.setItems(abonnesList);
    }

    private void reinitialiserFormulaire() {
        immatriculationField.clear();
        proprietaireField.clear();
        dateDebutPicker.setValue(null);
        dateFinPicker.setValue(null);
    }

    private void afficherMessage(String message, boolean erreur) {
        messageLabel.setText(message);
        messageLabel.setStyle(erreur ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
    }
}





