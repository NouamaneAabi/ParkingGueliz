# Configuration IntelliJ pour JavaFX

## Problème
L'erreur "des composants d'exécution JavaFX obligatoires sont manquants" se produit car JavaFX nécessite des arguments de module spécifiques.

## Solution : Configurer la Run Configuration dans IntelliJ

### Méthode 1 : Modifier la configuration existante (RECOMMANDÉ)

1. Dans IntelliJ, allez dans **Run → Edit Configurations...**
2. Sélectionnez votre configuration **MainApp** (ou créez-en une nouvelle)
3. Dans **VM options**, ajoutez ces arguments :

```
--module-path "C:\Users\amine\.m2\repository\org\openjfx\javafx-controls\17.0.2\javafx-controls-17.0.2-win.jar;C:\Users\amine\.m2\repository\org\openjfx\javafx-fxml\17.0.2\javafx-fxml-17.0.2-win.jar;C:\Users\amine\.m2\repository\org\openjfx\javafx-base\17.0.2\javafx-base-17.0.2-win.jar;C:\Users\amine\.m2\repository\org\openjfx\javafx-graphics\17.0.2\javafx-graphics-17.0.2-win.jar" --add-modules javafx.controls,javafx.fxml
```

4. Cliquez sur **Apply** puis **OK**
5. Relancez l'application

### Méthode 2 : Utiliser Maven JavaFX Plugin (PLUS SIMPLE)

1. Dans IntelliJ, ouvrez le panneau **Maven** (View → Tool Windows → Maven)
2. Développez **ParkingManager → Plugins → javafx**
3. Double-cliquez sur **javafx:run**
4. L'application devrait se lancer automatiquement !

### Méthode 3 : Créer une nouvelle Run Configuration Maven

1. **Run → Edit Configurations...**
2. Cliquez sur **+** → **Maven**
3. Nom : `Run JavaFX App`
4. Command line : `javafx:run`
5. Working directory : votre dossier de projet
6. Cliquez **OK** et lancez cette configuration

---

**La méthode 2 (Maven javafx:run) est la plus simple et recommandée !**

