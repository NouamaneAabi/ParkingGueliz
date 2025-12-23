#!/bin/bash

echo "========================================"
echo "  ParkingManager - Lancement"
echo "========================================"
echo ""

# Vérifier que Maven est installé
if ! command -v mvn &> /dev/null; then
    echo "ERREUR: Maven n'est pas installé"
    echo "Installez Maven: sudo apt-get install maven (Linux) ou brew install maven (Mac)"
    exit 1
fi

# Vérifier que MySQL est accessible
echo "Vérification de la connexion MySQL..."
echo ""

# Compiler et lancer l'application
echo "Compilation et lancement de l'application..."
echo ""
mvn clean javafx:run

if [ $? -ne 0 ]; then
    echo ""
    echo "ERREUR lors du lancement"
    echo "Vérifiez:"
    echo "1. Que MySQL est démarré"
    echo "2. Que la base de données 'parkingdb' existe"
    echo "3. Que les identifiants MySQL sont corrects dans hibernate.cfg.xml"
    exit 1
fi





