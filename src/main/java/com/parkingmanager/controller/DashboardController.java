package com.parkingmanager.controller;

import com.parkingmanager.model.Place;
import com.parkingmanager.service.ParkingService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.List;

public class DashboardController {
    @FXML
    private TableView<Place> placesTable;
    @FXML
    private TableColumn<Place, Integer> numeroColumn;
    @FXML
    private TableColumn<Place, String> statutColumn;
    @FXML
    private Label tauxOccupationLabel;
    @FXML
    private Label placesLibresLabel;
    @FXML
    private Label placesOccupeesLabel;

    private ParkingService parkingService;

    @FXML
    public void initialize() {
        parkingService = new ParkingService();
        setupTable();
        refreshData();
    }

    private void setupTable() {
        numeroColumn.setCellValueFactory(new PropertyValueFactory<>("numero"));
        statutColumn.setCellValueFactory(cellData -> {
            String statut = cellData.getValue().getStatut();
            return new javafx.beans.property.SimpleStringProperty(statut != null ? statut : "");
        });
    }

    private void setupTableColumns() {
        // Créer les colonnes from scratch
        TableColumn<Place, Integer> numCol = new TableColumn<>("Numéro");
        numCol.setPrefWidth(150);
        numCol.setCellValueFactory(new PropertyValueFactory<>("numero"));
        
        TableColumn<Place, String> statCol = new TableColumn<>("Statut");
        statCol.setPrefWidth(200);
        statCol.setCellValueFactory(cellData -> {
            String statut = cellData.getValue().getStatut();
            return new javafx.beans.property.SimpleStringProperty(statut != null ? statut : "");
        });
        
        placesTable.getColumns().addAll(numCol, statCol);
    }

    public void refreshData() {
        System.out.println("🔄 refreshData() appelée - rechargement depuis la DB...");
        
        // Exécuter sur le thread JavaFX pour éviter les problèmes de concurrence
        Platform.runLater(() -> {
            try {
                // Recharger les données depuis la DB
                List<Place> places = parkingService.getToutesLesPlaces();
                System.out.println("📍 Places chargées depuis DB: " + places.size() + " places");
                
                // Créer une NOUVELLE ObservableList avec les données fraîches
                ObservableList<Place> placesList = FXCollections.observableArrayList(places);
                
                // Réinitialiser la table complètement
                placesTable.getItems().clear();
                placesTable.getColumns().clear();
                
                // Recréer les colonnes
                setupTableColumns();
                
                // Réaffecter les données
                placesTable.setItems(placesList);
                
                // Force le rendu
                placesTable.refresh();
                System.out.println("✅ TableView mise à jour avec " + placesList.size() + " places");
                
                // Recalculer et afficher les stats depuis la DB
                updateStatistiques();
                
            } catch (Exception e) {
                System.err.println("❌ Erreur dans refreshData(): " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
    
    private void updateStatistiques() {
        try {
            // Recharger depuis la DB directement via les DAO
            double tauxOccupation = parkingService.calculerTauxOccupation();
            int libres = parkingService.getPlacesLibres().size();
            int occupees = parkingService.getPlacesOccupees().size();
            
            System.out.println("📊 Stats recalculées: taux=" + tauxOccupation + "%, libres=" + libres + ", occupées=" + occupees);
            
            tauxOccupationLabel.setText(String.format("%.1f%%", tauxOccupation));
            placesLibresLabel.setText(String.valueOf(libres));
            placesOccupeesLabel.setText(String.valueOf(occupees));
            
            System.out.println("✅ Statistiques mises à jour");
        } catch (Exception e) {
            System.err.println("❌ Erreur lors du calcul des statistiques: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void ouvrirEntreeSortie() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/entree-sortie.fxml"));
        Parent root = loader.load();
        // Pass reference of this dashboard to the EntreeSortieController so it can refresh
        com.parkingmanager.controller.EntreeSortieController controller = loader.getController();
        controller.setDashboardController(this);

        Stage stage = new Stage();
        stage.setTitle("Entrée/Sortie");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void ouvrirAbonnes() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/abonnes.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Gestion des Abonnés");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void ouvrirRecettes() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/recettes.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Recettes");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void ouvrirHistorique() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/historique.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Historique des Transactions");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void ouvrirParametres() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/parametres.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Paramètres du Parking");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void seDeconnecter(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setTitle("Gueliz Parking - Bienvenue");
        stage.setScene(new Scene(root));
        stage.centerOnScreen();
        stage.show();
    }
}





