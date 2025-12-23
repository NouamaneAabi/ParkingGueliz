-- Script SQL pour créer la base de données ParkingManager
-- Exécutez ce script dans MySQL Workbench ou via la ligne de commande MySQL

-- Créer la base de données
CREATE DATABASE IF NOT EXISTS parkingdb 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- Utiliser la base de données
USE parkingdb;

-- Note: Les tables seront créées automatiquement par Hibernate
-- lors du premier lancement de l'application grâce à la propriété
-- hibernate.hbm2ddl.auto=update dans hibernate.cfg.xml

-- Pour vérifier que la base de données est créée :
SHOW DATABASES LIKE 'parkingdb';





