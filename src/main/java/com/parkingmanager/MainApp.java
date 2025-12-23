package com.parkingmanager;

import com.parkingmanager.dao.*;
import com.parkingmanager.model.*;
import com.parkingmanager.service.AbonnementService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.time.LocalDate;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialiser les données de test
        initialiserDonneesTest();

        // Charger la vue de sélection (Login/Role)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        Parent root = loader.load();
        
        primaryStage.setTitle("Gueliz Parking - Bienvenue");
        primaryStage.setScene(new Scene(root, 1000, 750));
        primaryStage.show();
    }

    private void initialiserDonneesTest() {
        PlaceDAO placeDAO = new PlaceDAO();
        VehiculeDAO vehiculeDAO = new VehiculeDAO();
        AbonnementService abonnementService = new AbonnementService();

        // Créer des places si elles n'existent pas
        try {
            placeDAO.createTableIfNotExists();
        if (placeDAO.findAll().isEmpty()) {
            for (int i = 1; i <= 20; i++) {
                    Place place = new Place(i, "LIBRE");
                    placeDAO.insert(place);
            }
            System.out.println("20 places créées");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la création des places: " + e.getMessage());
        }

        // Créer quelques véhicules et abonnés de test
        if (vehiculeDAO.findAll().isEmpty()) {
            try {
                // Véhicule 1 avec abonnement actif
                Vehicule v1 = new Vehicule("AB-123-CD", "Jean Dupont");
                vehiculeDAO.save(v1);
                abonnementService.creerAbonnement("AB-123-CD", 
                    LocalDate.now().minusMonths(1), 
                    LocalDate.now().plusMonths(1));

                // Véhicule 2 avec abonnement actif
                Vehicule v2 = new Vehicule("EF-456-GH", "Marie Martin");
                vehiculeDAO.save(v2);
                abonnementService.creerAbonnement("EF-456-GH", 
                    LocalDate.now().minusDays(10), 
                    LocalDate.now().plusDays(20));

                // Véhicule 3 sans abonnement
                Vehicule v3 = new Vehicule("IJ-789-KL", "Pierre Durand");
                vehiculeDAO.save(v3);

                // Véhicule 4 avec abonnement expiré
                Vehicule v4 = new Vehicule("MN-012-OP", "Sophie Bernard");
                vehiculeDAO.save(v4);
                abonnementService.creerAbonnement("MN-012-OP", 
                    LocalDate.now().minusMonths(3), 
                    LocalDate.now().minusMonths(1));

                System.out.println("Données de test créées avec succès");
            } catch (Exception e) {
                System.err.println("Erreur lors de la création des données de test: " + e.getMessage());
            }
        }
    }

    @Override
    public void stop() throws Exception {
        GenericDAO.closeSessionFactory();
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

