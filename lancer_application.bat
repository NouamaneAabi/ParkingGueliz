@echo off
echo ========================================
echo   ParkingManager - Lancement
echo ========================================
echo.

REM Vérifier que Maven est installé
where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ERREUR: Maven n'est pas installé ou n'est pas dans le PATH
    echo Veuillez installer Maven ou l'ajouter au PATH
    pause
    exit /b 1
)

REM Vérifier que MySQL est accessible
echo Vérification de la connexion MySQL...
echo.

REM Compiler et lancer l'application
echo Compilation et lancement de l'application...
echo.
mvn clean javafx:run

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERREUR lors du lancement
    echo Vérifiez:
    echo 1. Que MySQL est démarré
    echo 2. Que la base de données 'parkingdb' existe
    echo 3. Que les identifiants MySQL sont corrects dans hibernate.cfg.xml
    pause
)





