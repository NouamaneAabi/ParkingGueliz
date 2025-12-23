package com.parkingmanager.dao;

import com.parkingmanager.model.Place;
import com.parkingmanager.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO simple pour gérer les places de parking avec JDBC
 */
public class PlaceDAO {
    
    /**
     * Crée la table places si elle n'existe pas
     */
    public void createTableIfNotExists() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS places (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                numero INT NOT NULL UNIQUE,
                statut VARCHAR(10) NOT NULL DEFAULT 'LIBRE',
                CHECK (statut IN ('LIBRE', 'OCCUPEE'))
            )
            """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("✅ Table 'places' créée ou existe déjà.");
        }
    }

    /**
     * Insère une nouvelle place
     */
    public void insert(Place place) throws SQLException {
        String sql = "INSERT INTO places (numero, statut) VALUES (?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, place.getNumero());
            pstmt.setString(2, place.getStatut());
            pstmt.executeUpdate();
            System.out.println("✅ Place " + place.getNumero() + " insérée.");
        }
    }

    /**
     * Récupère toutes les places (avec invalidation du cache Hibernate)
     */
    public List<Place> findAll() throws SQLException {
        // Vider le cache Hibernate pour forcer la lecture depuis la DB
        try {
            org.hibernate.Session session = GenericDAO.getSessionFactory().openSession();
            session.clear();
            session.close();
        } catch (Exception e) {
            System.err.println("⚠️ Impossible de vider le cache Hibernate: " + e.getMessage());
        }
        
        List<Place> places = new ArrayList<>();
        String sql = "SELECT * FROM places ORDER BY numero";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Place place = new Place(
                    rs.getLong("id"),
                    rs.getInt("numero"),
                    rs.getString("statut")
                );
                places.add(place);
            }
        }
        return places;
    }

    /**
     * Récupère une place par son numéro
     */
    public Place findByNumero(int numero) throws SQLException {
        String sql = "SELECT * FROM places WHERE numero = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, numero);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Place(
                        rs.getLong("id"),
                        rs.getInt("numero"),
                        rs.getString("statut")
                    );
                }
            }
        }
        return null;
    }

    /**
     * Met à jour le statut d'une place
     */
    public void updateStatut(int numero, String statut) throws SQLException {
        String sql = "UPDATE places SET statut = ? WHERE numero = ?";
        Connection conn = DatabaseConnection.getConnection();
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, statut);
            pstmt.setInt(2, numero);
            int rowsUpdated = pstmt.executeUpdate();
            
            // Forcer le commit pour s'assurer que les changements sont persistés
            if (!conn.getAutoCommit()) {
                conn.commit();
            }
            
            System.out.println("✅ Place " + numero + " mise à jour : " + statut + " (" + rowsUpdated + " lignes modifiées)");
            
            if (rowsUpdated == 0) {
                System.err.println("⚠️ ATTENTION: Aucune place trouvée avec le numéro " + numero);
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la mise à jour de la place " + numero + " : " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Compte les places libres
     */
    public int countPlacesLibres() throws SQLException {
        String sql = "SELECT COUNT(*) FROM places WHERE statut = 'LIBRE'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    /**
     * Compte les places occupées
     */
    public int countPlacesOccupees() throws SQLException {
        String sql = "SELECT COUNT(*) FROM places WHERE statut = 'OCCUPEE'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
}
