# Script de lancement simple
$ErrorActionPreference = "Continue"

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  LANCEMENT PARKING MANAGER" -ForegroundColor Cyan  
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Configurer Java
$env:JAVA_HOME = "C:\Users\amine\.jdks\corretto-23.0.2"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"

Write-Host "Java configuré: $env:JAVA_HOME" -ForegroundColor Green
Write-Host ""

# Vérifier Java
try {
    $javaVersion = & "$env:JAVA_HOME\bin\java.exe" -version 2>&1 | Select-Object -First 1
    Write-Host "Java version: $javaVersion" -ForegroundColor Gray
} catch {
    Write-Host "ERREUR: Java non accessible" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "Recherche de Maven..." -ForegroundColor Yellow

# Chercher Maven
$mvn = Get-Command mvn -ErrorAction SilentlyContinue

if ($mvn) {
    Write-Host "✅ Maven trouvé: $($mvn.Source)" -ForegroundColor Green
    Write-Host ""
    Write-Host "Compilation et lancement de l'application..." -ForegroundColor Yellow
    Write-Host ""
    
    # Lancer avec Maven
    & mvn clean javafx:run
    exit $LASTEXITCODE
} else {
    Write-Host "❌ Maven non trouvé dans PATH" -ForegroundColor Red
    Write-Host ""
    Write-Host "════════════════════════════════════════" -ForegroundColor Cyan
    Write-Host "  SOLUTION" -ForegroundColor Cyan
    Write-Host "════════════════════════════════════════" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Double-cliquez sur le fichier:" -ForegroundColor Yellow
    Write-Host "  LANCER_ICI.bat" -ForegroundColor White
    Write-Host ""
    Write-Host "OU utilisez IntelliJ IDEA:" -ForegroundColor Yellow
    Write-Host "  1. Ouvrez IntelliJ IDEA" -ForegroundColor White
    Write-Host "  2. Ouvrez ce projet" -ForegroundColor White
    Write-Host "  3. Ouvrez: src\main\java\com\parkingmanager\MainApp.java" -ForegroundColor White
    Write-Host "  4. Clic droit > Run 'MainApp.main()'" -ForegroundColor White
    Write-Host ""
    Write-Host "════════════════════════════════════════" -ForegroundColor Cyan
    Write-Host ""
    Read-Host "Appuyez sur Entrée pour quitter"
}

