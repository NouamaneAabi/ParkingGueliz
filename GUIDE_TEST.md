# Guide de Test - ParkingManager

## 📋 Étape 1 : Préparation de la Base de Données MySQL

### 1.1 Vérifier que MySQL est installé et démarré

Ouvrez un terminal et vérifiez :
```bash
mysql --version
```

### 1.2 Créer la base de données

Connectez-vous à MySQL :
```bash
mysql -u root -p
```

Puis exécutez :
```sql
CREATE DATABASE IF NOT EXISTS parkingdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE parkingdb;
SHOW DATABASES;
EXIT;
```

### 1.3 Configurer les identifiants MySQL

Éditez le fichier `src/main/resources/hibernate.cfg.xml` et modifiez les lignes 11-12 :
```xml
<property name="hibernate.connection.username">root</property>
<property name="hibernate.connection.password">VOTRE_MOT_DE_PASSE</property>
```

**Important** : Remplacez `root` par votre nom d'utilisateur MySQL et `VOTRE_MOT_DE_PASSE` par votre mot de passe MySQL.

## 📦 Étape 2 : Installation des Dépendances Maven

Ouvrez un terminal dans le dossier du projet et exécutez :

```bash
mvn clean install
```

Cela va télécharger toutes les dépendances (JavaFX, Hibernate, MySQL).

## 🚀 Étape 3 : Lancer l'Application

### Option A : Avec Maven (Recommandé)
```bash
mvn javafx:run
```

### Option B : Depuis IntelliJ IDEA
1. Ouvrez le projet dans IntelliJ IDEA
2. Attendez que Maven télécharge les dépendances
3. Ouvrez `src/main/java/com/parkingmanager/MainApp.java`
4. Cliquez droit sur la classe → `Run 'MainApp.main()'`

### Option C : Avec Java directement
```bash
mvn compile
java --module-path "C:\path\to\javafx-sdk\lib" --add-modules javafx.controls,javafx.fxml -cp "target/classes;target/dependency/*" com.parkingmanager.MainApp
```

## ✅ Étape 4 : Tests des Fonctionnalités

### Test 1 : Vérifier le Tableau de Bord
1. L'application s'ouvre sur le tableau de bord
2. Vous devriez voir :
   - 20 places créées automatiquement
   - Taux d'occupation à 0%
   - 20 places libres, 0 occupées

### Test 2 : Enregistrer une Entrée
1. Cliquez sur le bouton **"Entrée/Sortie"**
2. Dans la section "Enregistrer une Entrée" :
   - Immatriculation : `XY-999-ZZ` (format: AB-123-CD)
   - Propriétaire : `Test User`
   - Place : Sélectionnez une place (ex: 1)
3. Cliquez sur **"Enregistrer l'Entrée"**
4. Retournez au tableau de bord et cliquez sur **"Actualiser"**
5. Vérifiez que :
   - La place 1 est maintenant "OCCUPEE"
   - Le taux d'occupation a augmenté
   - Le nombre de places libres a diminué

### Test 3 : Enregistrer une Sortie
1. Retournez dans **"Entrée/Sortie"**
2. Dans la section "Enregistrer une Sortie" :
   - Immatriculation : `XY-999-ZZ`
3. Cliquez sur **"Enregistrer la Sortie"**
4. Vous devriez voir un montant affiché (ex: "Montant à payer: 1.00 €")
5. Retournez au tableau de bord et vérifiez que la place est redevenue "LIBRE"

### Test 4 : Gérer un Abonné
1. Cliquez sur **"Abonnés"**
2. Remplissez le formulaire :
   - Immatriculation : `AB-123-CD` (déjà créé par défaut)
   - Propriétaire : `Jean Dupont`
   - Date de début : Aujourd'hui
   - Date de fin : Dans 1 mois
3. Cliquez sur **"Créer l'Abonnement"**
4. Vérifiez que l'abonné apparaît dans le tableau avec le statut "Actif"

### Test 5 : Tester un Abonné Actif
1. Retournez dans **"Entrée/Sortie"**
2. Enregistrez une entrée pour `AB-123-CD` (l'abonné créé)
3. Enregistrez immédiatement la sortie
4. Vous devriez voir : **"Abonné actif - Aucun paiement requis"**

### Test 6 : Consulter les Recettes
1. Cliquez sur **"Recettes"**
2. Vous devriez voir :
   - Les recettes du jour
   - Le détail des paiements effectués
3. Changez la date pour voir les recettes d'autres jours

## 🐛 Résolution des Problèmes

### Erreur : "Cannot connect to MySQL"
- Vérifiez que MySQL est démarré
- Vérifiez les identifiants dans `hibernate.cfg.xml`
- Vérifiez que la base `parkingdb` existe

### Erreur : "JavaFX runtime components are missing"
- Vérifiez que Java 17+ est installé : `java -version`
- Utilisez `mvn javafx:run` au lieu de lancer directement

### Erreur : "Format d'immatriculation invalide"
- Le format doit être : `AB-123-CD` (2 lettres, tiret, 3 chiffres, tiret, 2 lettres)
- Exemples valides : `AB-123-CD`, `XY-999-ZZ`
- Exemples invalides : `AB123CD`, `ab-123-cd` (sera converti en majuscules)

### L'application ne se lance pas
1. Vérifiez les logs dans la console
2. Assurez-vous que toutes les dépendances Maven sont téléchargées
3. Vérifiez que le port 3306 n'est pas utilisé par autre chose

## 📊 Vérification dans MySQL

Pour vérifier les données dans MySQL :

```sql
USE parkingdb;

-- Voir toutes les places
SELECT * FROM places;

-- Voir tous les véhicules
SELECT * FROM vehicules;

-- Voir tous les tickets
SELECT * FROM tickets;

-- Voir tous les abonnés
SELECT * FROM abonnes;

-- Voir tous les paiements
SELECT * FROM paiements;
```

## 🎯 Scénario de Test Complet

1. **Créer 3 entrées** avec des véhicules différents
2. **Attendre 5 minutes** (ou modifier l'heure système pour tester)
3. **Enregistrer 2 sorties** et vérifier les montants calculés
4. **Créer un abonné** et tester qu'il ne paie pas
5. **Consulter les recettes** du jour
6. **Vérifier le taux d'occupation** sur le tableau de bord

Bon test ! 🚗





