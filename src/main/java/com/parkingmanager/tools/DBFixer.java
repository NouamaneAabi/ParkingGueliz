package com.parkingmanager.tools;

import com.parkingmanager.util.DatabaseConnection;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Statement;

public class DBFixer {
    public static void main(String[] args) {
        String path = "reset_and_recreate_tables.sql";
        if (args.length > 0) path = args[0];

        try {
            String sql = new String(Files.readAllBytes(Paths.get(path)));
            // split on semicolon followed by newline to avoid splitting inside strings
            String[] statements = sql.split(";\r?\n");

            try (Connection conn = DatabaseConnection.getConnection()) {
                conn.setAutoCommit(true);
                try (Statement stmt = conn.createStatement()) {
                    for (String s : statements) {
                        String trimmed = s.trim();
                        if (trimmed.isEmpty()) continue;
                        try {
                            stmt.execute(trimmed);
                        } catch (Exception ex) {
                            System.err.println("Échec exécution SQL: " + ex.getMessage());
                        }
                    }
                }
            }
            System.out.println("Script exécuté — vérifiez phpMyAdmin.");
        } catch (IOException e) {
            System.err.println("Impossible de lire le fichier SQL: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erreur: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
