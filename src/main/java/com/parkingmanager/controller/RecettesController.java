package com.parkingmanager.controller;

import com.parkingmanager.dao.PaiementDAO;
import com.parkingmanager.model.Paiement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RecettesController {
    @FXML
    private Label recettesDuJourLabel;
    @FXML
    private TableView<Paiement> paiementsTable;
    @FXML
    private TableColumn<Paiement, String> immatriculationColumn;
    @FXML
    private TableColumn<Paiement, Double> montantColumn;
    @FXML
    private TableColumn<Paiement, String> dateColumn;
    @FXML
    private DatePicker datePicker;

    private PaiementDAO paiementDAO;

    @FXML
    public void initialize() {
        paiementDAO = new PaiementDAO();
        setupTable();
        datePicker.setValue(LocalDate.now());
        chargerRecettes();
    }

    private void setupTable() {
        immatriculationColumn.setCellValueFactory(cellData -> {
            Paiement paiement = cellData.getValue();
            String immat = paiement.getTicket().getVehicule().getImmatriculation();
            return new javafx.beans.property.SimpleStringProperty(immat);
        });
        montantColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
        dateColumn.setCellValueFactory(cellData -> {
            Paiement paiement = cellData.getValue();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            String dateStr = paiement.getDate().format(formatter);
            return new javafx.beans.property.SimpleStringProperty(dateStr);
        });
    }

    @FXML
    private void chargerRecettes() {
        LocalDate date = datePicker.getValue();
        if (date == null) {
            date = LocalDate.now();
        }

        double recettes = paiementDAO.getRecettesDuJour(date);
        recettesDuJourLabel.setText(String.format("Recettes du jour: %.2f €", recettes));

        List<Paiement> paiements = paiementDAO.findPaiementsByDate(date);
        ObservableList<Paiement> paiementsList = FXCollections.observableArrayList(paiements);
        paiementsTable.setItems(paiementsList);
    }
}

