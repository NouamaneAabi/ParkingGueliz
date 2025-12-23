-- Script SQL simple pour créer la base de données
-- Exécutez ce script dans phpMyAdmin ou MySQL Workbench

-- Créer la base de données
CREATE DATABASE IF NOT EXISTS parkingdb 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- Utiliser la base de données
USE parkingdb;

-- Créer la table places
CREATE TABLE IF NOT EXISTS places (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero INT NOT NULL UNIQUE,
    statut VARCHAR(10) NOT NULL DEFAULT 'LIBRE',
    CHECK (statut IN ('LIBRE', 'OCCUPEE'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insérer 20 places de test (optionnel - l'application les crée automatiquement)
-- INSERT INTO places (numero, statut) VALUES
-- (1, 'LIBRE'), (2, 'LIBRE'), (3, 'LIBRE'), (4, 'LIBRE'), (5, 'LIBRE'),
-- (6, 'LIBRE'), (7, 'LIBRE'), (8, 'LIBRE'), (9, 'LIBRE'), (10, 'LIBRE'),
-- (11, 'LIBRE'), (12, 'LIBRE'), (13, 'LIBRE'), (14, 'LIBRE'), (15, 'LIBRE'),
-- (16, 'LIBRE'), (17, 'LIBRE'), (18, 'LIBRE'), (19, 'LIBRE'), (20, 'LIBRE');

-- Vérifier la création
SHOW TABLES;
SELECT * FROM places;




