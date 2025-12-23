package com.parkingmanager.controller;

import com.parkingmanager.model.Ticket;
import com.parkingmanager.service.ParkingService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class HistoriqueController {
    @FXML
    private TableView<Ticket> historyTable;
    @FXML
    private TableColumn<Ticket, String> immatriculationColumn;
    @FXML
    private TableColumn<Ticket, String> proprietaireColumn;
    @FXML
    private TableColumn<Ticket, String> placeColumn;
    @FXML
    private TableColumn<Ticket, String> entreeColumn;
    @FXML
    private TableColumn<Ticket, String> sortieColumn;
    @FXML
    private TableColumn<Ticket, String> montantColumn;

    private ParkingService parkingService;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @FXML
    public void initialize() {
        parkingService = new ParkingService();
        setupTable();
        refresh();
    }

    private void setupTable() {
        immatriculationColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getVehicule().getImmatriculation()));
        
        proprietaireColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getVehicule().getProprietaire()));
        
        placeColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(String.valueOf(cellData.getValue().getPlace().getNumero())));
        
        entreeColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getEntree().format(FORMATTER)));
        
        sortieColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue().getSortie() != null) {
                return new SimpleStringProperty(cellData.getValue().getSortie().format(FORMATTER));
            }
            return new SimpleStringProperty("En cours...");
        });
        
        montantColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue().getMontant() != null) {
                return new SimpleStringProperty(String.format("%.2f €", cellData.getValue().getMontant()));
            }
            return new SimpleStringProperty("-");
        });
    }

    @FXML
    public void refresh() {
        List<Ticket> tickets = parkingService.getHistoriqueTickets();
        ObservableList<Ticket> ticketsList = FXCollections.observableArrayList(tickets);
        // Trier par date d'entrée décroissante (plus récent en haut)
        ticketsList.sort((t1, t2) -> t2.getEntree().compareTo(t1.getEntree()));
        historyTable.setItems(ticketsList);
    }

    @FXML
    private void fermer() {
        Stage stage = (Stage) historyTable.getScene().getWindow();
        stage.close();
    }
}
