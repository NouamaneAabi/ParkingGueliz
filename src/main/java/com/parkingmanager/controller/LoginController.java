package com.parkingmanager.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class LoginController {

    @FXML
    private void selectionnerAdmin(ActionEvent event) throws IOException {
        chargerVue("/dashboard.fxml", "Tableau de Bord - Admin", event);
    }

    @FXML
    private void selectionnerAdmin(MouseEvent event) throws IOException {
        chargerVue("/dashboard.fxml", "Tableau de Bord - Admin", (Stage)((Node)event.getSource()).getScene().getWindow());
    }

    @FXML
    private void selectionnerClient(ActionEvent event) throws IOException {
        chargerVue("/client_paiement.fxml", "Borne de Paiement Client", event);
    }

    @FXML
    private void selectionnerClient(MouseEvent event) throws IOException {
        chargerVue("/client_paiement.fxml", "Borne de Paiement Client", (Stage)((Node)event.getSource()).getScene().getWindow());
    }

    private void chargerVue(String fxmlPath, String title, ActionEvent event) throws IOException {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        chargerVue(fxmlPath, title, stage);
    }

    private void chargerVue(String fxmlPath, String title, Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        
        Scene scene = new Scene(root);
        stage.setTitle("Gueliz Parking - " + title);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
}
