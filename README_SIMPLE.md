# ParkingManager - Version Simple Java + MySQL

Application Java simple avec connexion MySQL directe (JDBC), compatible JDK 23 et IntelliJ IDEA.

## 🎯 Caractéristiques

- ✅ 100% Java pur (pas de JavaFX, pas de Hibernate)
- ✅ Connexion MySQL simple avec JDBC
- ✅ Compatible JDK 23
- ✅ Compatible IntelliJ IDEA
- ✅ Interface console simple et claire
- ✅ Code simple et facile à comprendre

## 📋 Prérequis

1. **JDK 23** installé
2. **IntelliJ IDEA** (version récente)
3. **MySQL** avec phpMyAdmin
4. **Maven** (installé avec IntelliJ)

## 🚀 Installation Rapide

### Étape 1 : Créer la base de données

**Option A : Via phpMyAdmin**
1. Ouvrez phpMyAdmin dans votre navigateur
2. Cliquez sur "SQL"
3. Exécutez le script `database_setup.sql` fourni

**Option B : Via MySQL en ligne de commande**
```bash
mysql -u root -p
```
Puis exécutez :
```sql
CREATE DATABASE parkingdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE parkingdb;
```

### Étape 2 : Configurer la connexion MySQL

Éditez le fichier `src/main/java/com/parkingmanager/util/DatabaseConnection.java` :

```java
private static final String USERNAME = "root";  // Votre nom d'utilisateur MySQL
private static final String PASSWORD = "";      // Votre mot de passe MySQL
```

### Étape 3 : Importer le projet dans IntelliJ

1. Ouvrez IntelliJ IDEA
2. File → Open → Sélectionnez le dossier du projet
3. IntelliJ détectera automatiquement le `pom.xml` et téléchargera les dépendances Maven
4. Attendez que Maven termine le téléchargement

### Étape 4 : Configurer le JDK dans IntelliJ

1. File → Project Structure (Ctrl+Alt+Shift+S)
2. Project → SDK → Sélectionnez JDK 23
3. Project → Language level → 23

### Étape 5 : Lancer l'application

**Dans IntelliJ :**
1. Ouvrez `src/main/java/com/parkingmanager/Main.java`
2. Clic droit sur la classe → `Run 'Main.main()'`

**Ou via Maven en ligne de commande :**
```bash
mvn compile exec:java -Dexec.mainClass="com.parkingmanager.Main"
```

## 🎮 Utilisation

L'application affiche un menu simple :

```
╔═══════════════════════════════════════╗
║           MENU PRINCIPAL              ║
╠═══════════════════════════════════════╣
║ 1. Lister toutes les places          ║
║ 2. Voir les statistiques             ║
║ 3. Marquer une place comme occupée   ║
║ 4. Libérer une place                 ║
║ 5. Quitter                           ║
╚═══════════════════════════════════════╝
```

## 📁 Structure du Projet

```
ParkingManager/
├── src/main/java/com/parkingmanager/
│   ├── Main.java                    # Point d'entrée principal
│   ├── util/
│   │   └── DatabaseConnection.java  # Gestion de la connexion MySQL
│   ├── dao/
│   │   └── PlaceDAO.java           # Accès aux données (JDBC)
│   └── model/
│       └── Place.java              # Modèle de données simple
├── database_setup.sql              # Script SQL pour créer la base
├── pom.xml                         # Configuration Maven
└── README_SIMPLE.md               # Ce fichier
```

## 🔧 Résolution des Problèmes

### Erreur : "Cannot connect to MySQL"
- Vérifiez que MySQL est démarré
- Vérifiez les identifiants dans `DatabaseConnection.java`
- Vérifiez que la base `parkingdb` existe

### Erreur : "Driver MySQL non trouvé"
- Vérifiez que Maven a bien téléchargé les dépendances
- Dans IntelliJ : View → Tool Windows → Maven → Reimport

### Erreur : "Unsupported class file major version"
- Vérifiez que vous utilisez JDK 23
- Dans IntelliJ : File → Project Structure → Project → SDK → JDK 23

## ✅ Test Rapide

1. Lancez l'application
2. Vous devriez voir : `✅ Connexion à MySQL réussie !`
3. L'application crée automatiquement 20 places
4. Testez les fonctionnalités du menu

## 🎓 Prochaines Étapes

Vous pouvez facilement ajouter :
- Plus de tables (Véhicules, Tickets, etc.)
- Plus de fonctionnalités dans le menu
- Validation des données
- Gestion des erreurs plus avancée

Bon développement ! 🚀




