# ParkingManager - Application de Gestion de Parking

Application JavaFX avec Hibernate pour la gestion d'un parking (entrées, sorties, abonnements, tickets).

## Prérequis

- Java 17 ou supérieur
- Maven 3.6+
- MySQL 8.0+
- MySQL Workbench (optionnel)

## Configuration de la Base de Données

1. Créer une base de données MySQL :
```sql
CREATE DATABASE parkingdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. Configurer les paramètres de connexion dans `src/main/resources/hibernate.cfg.xml` :
   - Modifier `username` et `password` selon votre configuration MySQL
   - Vérifier que le port est correct (par défaut: 3306)

## Installation et Exécution

1. Cloner ou télécharger le projet

2. Installer les dépendances Maven :
```bash
mvn clean install
```

3. Lancer l'application :
```bash
mvn javafx:run
```

Ou depuis IntelliJ IDEA :
- Exécuter la classe `MainApp` dans `src/main/java/com/parkingmanager/MainApp.java`

## Structure du Projet

```
ParkingManager/
├── src/main/java/com/parkingmanager/
│   ├── model/          # Entités JPA/Hibernate
│   ├── dao/            # Accès aux données
│   ├── service/        # Logique métier
│   ├── controller/     # Contrôleurs JavaFX
│   └── MainApp.java    # Point d'entrée
├── src/main/resources/
│   ├── *.fxml          # Vues JavaFX
│   ├── hibernate.cfg.xml
│   └── persistence.xml
└── pom.xml
```

## Fonctionnalités

- **Tableau de bord** : Vue d'ensemble des places et statistiques
- **Entrée/Sortie** : Enregistrement des entrées et sorties de véhicules
- **Gestion des Abonnés** : Création et suivi des abonnements mensuels
- **Recettes** : Consultation des recettes journalières

## Règles de Gestion

- Format d'immatriculation : `AB-123-CD` (2 lettres, 3 chiffres, 2 lettres)
- Tarif : 2€ par heure (minimum 1€)
- Les abonnés actifs ne paient pas lors de la sortie
- Une place occupée ne peut pas accueillir un second véhicule
- Un véhicule ne peut avoir qu'un seul ticket ouvert à la fois

## Données de Test

L'application crée automatiquement :
- 20 places numérotées de 1 à 20
- 4 véhicules de test avec différents statuts d'abonnement

## Calcul des Tarifs

Le montant est calculé selon la durée de stationnement :
- Tarif horaire : 2€/heure
- Tarif minimum : 1€
- Calcul au prorata de la minute
- Gratuit pour les abonnés actifs

## Notes Techniques

- Hibernate gère automatiquement la création des tables (mode `update`)
- Les abonnements sont vérifiés à chaque sortie
- L'historique des tickets et paiements est conservé
Images De l application GuelizParking
<img width="929" height="648" alt="image" src="https://github.com/user-attachments/assets/41faa73c-d8bf-4800-bf67-7ec1ff13807a" />
<img width="945" height="686" alt="image" src="https://github.com/user-attachments/assets/ee2ab272-7aa4-48af-9b05-e13cd1b82881" />
<img width="709" height="632" alt="image" src="https://github.com/user-attachments/assets/d23e0c2d-9639-42ca-826a-d2671c519034" />
<img width="798" height="655" alt="image" src="https://github.com/user-attachments/assets/e6750544-51e6-49bf-881f-f858c5cc4bfb" />
<img width="928" height="686" alt="image" src="https://github.com/user-attachments/assets/04379100-e197-4fbd-bdae-b290eff2801f" />










