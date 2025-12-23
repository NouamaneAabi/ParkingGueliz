# Rapport de Projet : Système de Gestion de Parking "Gueliz Parking"

## 1. Introduction
Le projet **Gueliz Parking** est une application desktop robuste conçue pour automatiser et moderniser la gestion d'un parking. L'objectif principal est de fournir une interface intuitive pour les administrateurs tout en offrant une autonomie complète aux clients pour le paiement de leurs tickets via une borne automatique.

## 2. Architecture Technique
L'application repose sur une architecture multicouche (N-Tier) garantissant une séparation claire des responsabilités :
- **Modèle (Model)** : Entités persistantes (Véhicule, Place, Ticket, Paiement, Abonne, Configuration).
- **Accès aux Données (DAO)** : Utilisation d'**Hibernate ORM** pour l'interaction avec la base de données MySQL. Utilisation d'un `GenericDAO` pour la réutilisabilité.
- **Services** : Logique métier (Calcul des tarifs, gestion des entrées/sorties, validation des abonnements).
- **Contrôleurs (Controller)** : Gestion de l'interactivité JavaFX.
- **Vue (View)** : Interfaces définies en **FXML** et stylisées avec du **CSS** moderne.

## 3. Base de Données
Le schéma SQL comprend les tables suivantes :
- `vehicules` : Stocke les informations des voitures.
- `places` : Gère l'état (Libre/Occupée) des emplacements.
- `tickets` : Enregistre les sessions de stationnement (heure entrée/sortie, montant).
- `paiements` : Archive les transactions financières.
- `abonnes` : Gère les clients réguliers avec abonnements actifs.
- `configurations` : Permet le stockage dynamique des paramètres (tarifs horaires, etc.).

## 4. Fonctionnalités Clés

### 4.1 Interface de Bienvenue & Choix des Rôles
Au lancement, l'application propose deux modes d'accès distincts :
- **Mode Administrateur** : Pour la gestion opérationnelle.
- **Mode Client** : Interface simplifiée type "Borne de paiement".

### 4.2 Tableau de Bord Administrateur
- **Suivi en Temps Réel** : Visualisation du taux d'occupation et des recettes du jour.
- **Gestion des Entrées/Sorties** : Enregistrement rapide des véhicules.
- **Historique des Transactions** : Consultation de tous les tickets passés avec détails complets.
- **Paramètres Dynamiques** : Modification en temps réel du tarif horaire et du tarif minimum sans redémarrage de l'application.

### 4.3 Espace Client (Borne Automatique)
- Recherche simplifiée par immatriculation.
- Calcul automatique de la durée et du montant dû.
- Simulation de paiement sécurisé par carte bancaire.
- Libération automatique de la place après validation.

## 5. Design et Expérience Utilisateur (UX/UI)
L'application utilise un thème **"Digital Blue"** moderne :
- **Composants en "Cards"** : Pour une meilleure lisibilité des informations.
- **CSS Personnalisé** : Utilisation de dégradés, d'effets de survol (hover) et de polices modernes (Inter/Roboto).
- **Feedback Visuel** : Alertes et messages de confirmation pour chaque action critique.

## 6. Détails d'Implémentation Notables
- **CalculTarifService** : Implémente une logique qui vérifie d'abord si le véhicule possède un abonnement actif (gratuité) avant d'appliquer les tarifs configurés en base de données.
- **Validation** : Expressions régulières pour les immatriculations et contrôles de saisie rigoureux pour les montants.

## 7. Conclusion
Le système **Gueliz Parking** offre une solution clé en main, alliant performance technique (Hibernate/MySQL) et esthétique moderne. La séparation des rôles (Admin/Client) en fait un outil prêt pour un déploiement réel dans un environnement de stationnement moderne.

---
**Réalisé par :** Amine et Nouamane
**Version :** 2.0
**Technos :** Java 17+, JavaFX, Hibernate, MySQL, CSS.
