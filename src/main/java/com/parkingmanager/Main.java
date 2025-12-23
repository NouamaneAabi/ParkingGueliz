package com.parkingmanager;

import com.parkingmanager.dao.PlaceDAO;
import com.parkingmanager.model.Place;
import com.parkingmanager.util.DatabaseConnection;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * Application principale simple pour tester la connexion MySQL et gérer le parking
 */
public class Main {
    private static PlaceDAO placeDAO = new PlaceDAO();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   ParkingManager - Gestion Simple     ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println();

        // Test de connexion
        if (!DatabaseConnection.testConnection()) {
            System.err.println("❌ Impossible de se connecter à MySQL.");
            System.err.println("Vérifiez :");
            System.err.println("1. Que MySQL est démarré");
            System.err.println("2. Que la base 'parkingdb' existe");
            System.err.println("3. Les identifiants dans DatabaseConnection.java");
            return;
        }

        try {
            // Créer les tables
            placeDAO.createTableIfNotExists();
            
            // Initialiser les places si nécessaire
            initializePlaces();

            // Menu principal
            showMenu();

        } catch (SQLException e) {
            System.err.println("❌ Erreur SQL : " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection();
        }
    }

    /**
     * Initialise les places si la base est vide
     */
    private static void initializePlaces() throws SQLException {
        List<Place> places = placeDAO.findAll();
        if (places.isEmpty()) {
            System.out.println("📝 Initialisation de 20 places...");
            for (int i = 1; i <= 20; i++) {
                Place place = new Place(i, "LIBRE");
                placeDAO.insert(place);
            }
            System.out.println("✅ 20 places créées avec succès !\n");
        } else {
            System.out.println("✅ " + places.size() + " places trouvées dans la base.\n");
        }
    }

    /**
     * Affiche le menu principal
     */
    private static void showMenu() throws SQLException {
        while (true) {
            System.out.println("\n╔═══════════════════════════════════════╗");
            System.out.println("║           MENU PRINCIPAL              ║");
            System.out.println("╠═══════════════════════════════════════╣");
            System.out.println("║ 1. Lister toutes les places          ║");
            System.out.println("║ 2. Voir les statistiques             ║");
            System.out.println("║ 3. Marquer une place comme occupée   ║");
            System.out.println("║ 4. Libérer une place                 ║");
            System.out.println("║ 5. Quitter                           ║");
            System.out.println("╚═══════════════════════════════════════╝");
            System.out.print("\nVotre choix : ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consommer le retour à la ligne

            switch (choice) {
                case 1 -> listAllPlaces();
                case 2 -> showStatistics();
                case 3 -> occupyPlace();
                case 4 -> freePlace();
                case 5 -> {
                    System.out.println("\n👋 Au revoir !");
                    return;
                }
                default -> System.out.println("❌ Choix invalide !");
            }
        }
    }

    /**
     * Liste toutes les places
     */
    private static void listAllPlaces() throws SQLException {
        System.out.println("\n📋 Liste des places :");
        System.out.println("─".repeat(50));
        List<Place> places = placeDAO.findAll();
        
        for (Place place : places) {
            String emoji = place.getStatut().equals("LIBRE") ? "🟢" : "🔴";
            System.out.printf("%s Place %d : %s%n", emoji, place.getNumero(), place.getStatut());
        }
        System.out.println("─".repeat(50));
        System.out.println("Total : " + places.size() + " places");
    }

    /**
     * Affiche les statistiques
     */
    private static void showStatistics() throws SQLException {
        int libres = placeDAO.countPlacesLibres();
        int occupees = placeDAO.countPlacesOccupees();
        int total = libres + occupees;
        double taux = total > 0 ? (occupees * 100.0 / total) : 0.0;

        System.out.println("\n📊 Statistiques :");
        System.out.println("─".repeat(50));
        System.out.printf("Places libres   : %d 🟢%n", libres);
        System.out.printf("Places occupées : %d 🔴%n", occupees);
        System.out.printf("Total           : %d%n", total);
        System.out.printf("Taux occupation : %.1f%%%n", taux);
        System.out.println("─".repeat(50));
    }

    /**
     * Marque une place comme occupée
     */
    private static void occupyPlace() throws SQLException {
        System.out.print("\nNuméro de la place à occuper : ");
        int numero = scanner.nextInt();
        scanner.nextLine();

        Place place = placeDAO.findByNumero(numero);
        if (place == null) {
            System.out.println("❌ Place non trouvée !");
            return;
        }

        if (place.getStatut().equals("OCCUPEE")) {
            System.out.println("❌ Cette place est déjà occupée !");
            return;
        }

        placeDAO.updateStatut(numero, "OCCUPEE");
        System.out.println("✅ Place " + numero + " marquée comme occupée !");
    }

    /**
     * Libère une place
     */
    private static void freePlace() throws SQLException {
        System.out.print("\nNuméro de la place à libérer : ");
        int numero = scanner.nextInt();
        scanner.nextLine();

        Place place = placeDAO.findByNumero(numero);
        if (place == null) {
            System.out.println("❌ Place non trouvée !");
            return;
        }

        if (place.getStatut().equals("LIBRE")) {
            System.out.println("❌ Cette place est déjà libre !");
            return;
        }

        placeDAO.updateStatut(numero, "LIBRE");
        System.out.println("✅ Place " + numero + " libérée !");
    }
}

