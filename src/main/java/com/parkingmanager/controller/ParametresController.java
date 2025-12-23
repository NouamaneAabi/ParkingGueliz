package com.parkingmanager.controller;

import com.parkingmanager.dao.ConfigurationDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ParametresController {
    @FXML
    private TextField tarifHoraireField;
    @FXML
    private TextField tarifMinimumField;

    private ConfigurationDAO configDAO;

    @FXML
    public void initialize() {
        configDAO = new ConfigurationDAO();
        chargerParametres();
    }

    private void chargerParametres() {
        tarifHoraireField.setText(configDAO.getValue("tarif_heure", "2.0"));
        tarifMinimumField.setText(configDAO.getValue("tarif_minimum", "1.0"));
    }

    @FXML
    private void enregistrer() {
        try {
            double horaire = Double.parseDouble(tarifHoraireField.getText().replace(",", "."));
            double minimum = Double.parseDouble(tarifMinimumField.getText().replace(",", "."));

            if (horaire < 0 || minimum < 0) {
                throw new NumberFormatException("Les tarifs doivent être positifs.");
            }

            configDAO.setValue("tarif_heure", String.valueOf(horaire));
            configDAO.setValue("tarif_minimum", String.valueOf(minimum));

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Paramètres enregistrés avec succès !");
            alert.showAndWait();

            annuler(); // Fermer la fenêtre
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de format");
            alert.setHeaderText("Entrée invalide");
            alert.setContentText("Veuillez saisir des nombres valides pour les tarifs.");
            alert.showAndWait();
        }
    }

    @FXML
    private void annuler() {
        Stage stage = (Stage) tarifHoraireField.getScene().getWindow();
        stage.close();
    }
}
