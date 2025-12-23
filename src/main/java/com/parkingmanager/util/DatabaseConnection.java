package com.parkingmanager.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe simple pour gérer la connexion à MySQL
 */
public class DatabaseConnection {
    // Configuration de la base de données (valeurs par défaut)
    // Remarque: certains environnements utilisent le nom de base 'parking_db' (avec underscore).
    // On met cette valeur par défaut pour correspondre à votre installation phpMyAdmin.
    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/parking_db?useSSL=false&serverTimezone=UTC";
    private static final String DEFAULT_USERNAME = "root";
    private static final String DEFAULT_PASSWORD = "";
    
    private static Connection connection = null;

    /**
     * Établit une connexion à la base de données MySQL
     * @return Connection à la base de données
     * @throws SQLException si la connexion échoue
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Lire les identifiants depuis les variables d'environnement si présentes
                String url = System.getenv().getOrDefault("DB_URL", DEFAULT_URL);
                String user = System.getenv().getOrDefault("DB_USER", DEFAULT_USERNAME);
                String pass = System.getenv().getOrDefault("DB_PASS", DEFAULT_PASSWORD);

                // Charger le driver MySQL
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Établir la connexion
                connection = DriverManager.getConnection(url, user, pass);
                System.out.println("✅ Connexion à MySQL réussie ! (" + url + ")");
                // Vérifier la présence de la colonne 'statut' dans la table 'places' et la créer si nécessaire
                try (java.sql.Statement s = connection.createStatement()) {
                    String checkColumn = "SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'places' AND COLUMN_NAME = 'statut'";
                    try (java.sql.ResultSet rs = s.executeQuery(checkColumn)) {
                        if (rs.next()) {
                            int count = rs.getInt(1);
                            if (count == 0) {
                                System.out.println("➡️ Colonne 'statut' absente dans 'places' : ajout en cours...");
                                s.executeUpdate("ALTER TABLE places ADD COLUMN statut VARCHAR(20) NOT NULL DEFAULT 'LIBRE'");
                                System.out.println("✅ Colonne 'statut' ajoutée à 'places'.");
                            }
                        }
                    }
                } catch (SQLException ex) {
                    System.err.println("⚠️ Impossible de vérifier/créer la colonne 'statut' : " + ex.getMessage());
                }

                // Si un script reset_and_recreate_tables.sql est présent dans le répertoire de travail,
                // l'exécuter une fois pour réinitialiser/recréer vehicules/abonnes/paiements.
                try {
                    java.nio.file.Path script = java.nio.file.Paths.get("reset_and_recreate_tables.sql");
                    if (java.nio.file.Files.exists(script)) {
                        System.out.println("➡️ Script de reset détecté — exécution en cours...");
                        String sql = new String(java.nio.file.Files.readAllBytes(script));
                        String[] stmts = sql.split(";\\r?\\n");
                        try (java.sql.Statement st = connection.createStatement()) {
                            for (String ssql : stmts) {
                                String t = ssql.trim();
                                if (t.isEmpty()) continue;
                                try { st.execute(t); } catch (Exception ex) { System.err.println("SQL ignored: " + ex.getMessage()); }
                            }
                        }
                        // renommer le script pour ne pas le relancer
                        java.nio.file.Files.move(script, script.resolveSibling("reset_and_recreate_tables.sql.done"), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                        System.out.println("✅ Script reset exécuté et renommé en reset_and_recreate_tables.sql.done");
                    }
                } catch (Exception ex) {
                    System.err.println("❌ Échec de l'exécution du script de reset : " + ex.getMessage());
                }
            } catch (ClassNotFoundException e) {
                System.err.println("❌ Driver MySQL non trouvé : " + e.getMessage());
                throw new SQLException("Driver MySQL non trouvé", e);
            } catch (SQLException e) {
                System.err.println("❌ Erreur de connexion à MySQL : " + e.getMessage());
                // Ne pas basculer sur H2 — forcer l'échec pour s'assurer que MySQL est disponible
                throw e;
            }
        }
        return connection;
    }

    /**
     * Ferme la connexion à la base de données
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("✅ Connexion fermée.");
            } catch (SQLException e) {
                System.err.println("❌ Erreur lors de la fermeture de la connexion : " + e.getMessage());
            }
        }
    }

    /**
     * Teste la connexion à la base de données
     */
    public static boolean testConnection() {
        try {
            Connection conn = getConnection();
            boolean isValid = conn != null && !conn.isClosed();
            System.out.println("Test de connexion : " + (isValid ? "✅ Réussi" : "❌ Échoué"));
            return isValid;
        } catch (SQLException e) {
            System.err.println("❌ Test de connexion échoué : " + e.getMessage());
            return false;
        }
    }
}

