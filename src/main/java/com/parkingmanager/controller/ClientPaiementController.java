package com.parkingmanager.controller;

import com.parkingmanager.dao.TicketDAO;
import com.parkingmanager.dao.VehiculeDAO;
import com.parkingmanager.model.Paiement;
import com.parkingmanager.model.Ticket;
import com.parkingmanager.model.Vehicule;
import com.parkingmanager.service.CalculTarifService;
import com.parkingmanager.service.ParkingService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ClientPaiementController {
    @FXML private TextField immatriculationSearchField;
    @FXML private VBox ticketInfoBox;
    @FXML private Label placeLabel;
    @FXML private Label arriveeLabel;
    @FXML private Label dureeLabel;
    @FXML private Label totalLabel;

    @FXML private VBox paymentBox;
    @FXML private TextField cardNameField;
    @FXML private TextField cardNumberField;
    @FXML private TextField cardExpiryField;
    @FXML private PasswordField cardCvcField;
    @FXML private Button payButton;

    private ParkingService parkingService;
    private CalculTarifService calculTarifService;
    private VehiculeDAO vehiculeDAO;
    private TicketDAO ticketDAO;
    
    private Ticket currentTicket;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @FXML
    public void initialize() {
        parkingService = new ParkingService();
        calculTarifService = new CalculTarifService();
        vehiculeDAO = new VehiculeDAO();
        ticketDAO = new TicketDAO();
    }

    @FXML
    private void rechercherTicket() {
        String immat = immatriculationSearchField.getText().trim().toUpperCase();
        if (immat.isEmpty()) {
            showAlert("Erreur", "Veuillez entrer une immatriculation.", Alert.AlertType.ERROR);
            return;
        }

        Vehicule v = vehiculeDAO.findByImmatriculation(immat);
        if (v == null) {
            showAlert("Non trouvé", "Aucun véhicule trouvé avec cette immatriculation.", Alert.AlertType.WARNING);
            return;
        }

        currentTicket = ticketDAO.findTicketOuvertByVehicule(v.getId());
        if (currentTicket == null) {
            showAlert("Information", "Ce véhicule n'a pas de ticket en cours d'occupation.", Alert.AlertType.INFORMATION);
            return;
        }

        // Simuler le calcul à l'instant T
        currentTicket.setSortie(LocalDateTime.now());
        double montant = calculTarifService.calculerMontant(currentTicket);
        
        // Afficher les infos
        placeLabel.setText("Place " + currentTicket.getPlace().getNumero());
        arriveeLabel.setText(currentTicket.getEntree().format(FORMATTER));
        
        Duration d = Duration.between(currentTicket.getEntree(), currentTicket.getSortie());
        long hours = d.toHours();
        long minutes = d.toMinutesPart();
        dureeLabel.setText(String.format("%dh %02dmin", hours, minutes));
        
        totalLabel.setText(String.format("%.2f €", montant));
        
        ticketInfoBox.setVisible(true);
        paymentBox.setVisible(true);
    }

    @FXML
    private void procederPaiement() {
        if (currentTicket == null) return;

        // Validation basique de la carte
        if (cardNameField.getText().isEmpty() || cardNumberField.getText().length() < 16 || 
            cardExpiryField.getText().isEmpty() || cardCvcField.getText().length() < 3) {
            showAlert("Paiement refusé", "Veuillez remplir correctement les informations de votre carte bancaire.", Alert.AlertType.ERROR);
            return;
        }

        try {
            // Utiliser le service pour enregistrer la sortie réelle
            Paiement p = parkingService.enregistrerSortie(currentTicket.getVehicule().getImmatriculation());
            
            showAlert("Succès", "Paiement effectué avec succès ! Merci de votre visite.", Alert.AlertType.INFORMATION);
            
            fermer();
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur est survenue lors du paiement : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void fermer() {
        Stage stage = (Stage) immatriculationSearchField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
