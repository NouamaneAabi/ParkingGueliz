-- Script SQL complet pour créer TOUTES les tables ParkingManager
-- Exécutez ce script dans phpMyAdmin ou via MySQL CLI

USE parkingdb;

-- Table vehicules
CREATE TABLE IF NOT EXISTS vehicules (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    immatriculation VARCHAR(20) NOT NULL UNIQUE,
    proprietaire VARCHAR(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Table places
CREATE TABLE IF NOT EXISTS places (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero INT NOT NULL UNIQUE,
    statut VARCHAR(20) NOT NULL DEFAULT 'LIBRE'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Table tickets
CREATE TABLE IF NOT EXISTS tickets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    entree TIMESTAMP NOT NULL,
    sortie TIMESTAMP,
    montant DOUBLE,
    place_id BIGINT NOT NULL,
    vehicule_id BIGINT NOT NULL,
    FOREIGN KEY (place_id) REFERENCES places(id),
    FOREIGN KEY (vehicule_id) REFERENCES vehicules(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Table abonnes
CREATE TABLE IF NOT EXISTS abonnes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    dateDebut DATE NOT NULL,
    dateFin DATE NOT NULL,
    vehicule_id BIGINT NOT NULL UNIQUE,
    FOREIGN KEY (vehicule_id) REFERENCES vehicules(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Table paiements
CREATE TABLE IF NOT EXISTS paiements (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    montant DOUBLE NOT NULL,
    date TIMESTAMP NOT NULL,
    ticket_id BIGINT NOT NULL,
    FOREIGN KEY (ticket_id) REFERENCES tickets(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Afficher les tables créées
SHOW TABLES;
