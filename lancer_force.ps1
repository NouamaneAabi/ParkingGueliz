# Script pour forcer le lancement - essaie toutes les méthodes possibles
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  ParkingManager - Lancement FORCE" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Méthode 1: Essayer Maven directement
Write-Host "[1/4] Tentative avec Maven..." -ForegroundColor Yellow
try {
    $mvnOutput = & mvn --version 2>&1
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ Maven trouvé!" -ForegroundColor Green
        Write-Host "Lancement de l'application..." -ForegroundColor Yellow
        mvn clean javafx:run
        exit $LASTEXITCODE
    }
} catch {
    Write-Host "❌ Maven non disponible" -ForegroundColor Red
}

# Méthode 2: Chercher Java dans tous les emplacements possibles
Write-Host ""
Write-Host "[2/4] Recherche approfondie de Java..." -ForegroundColor Yellow
$javaPaths = @(
    "$env:JAVA_HOME\bin\java.exe",
    "C:\Program Files\Java\jdk-*\bin\java.exe",
    "C:\Program Files\Java\jdk*\bin\java.exe",
    "C:\Program Files (x86)\Java\jdk-*\bin\java.exe",
    "$env:LOCALAPPDATA\Programs\Eclipse Adoptium\jdk-*\bin\java.exe",
    "$env:ProgramFiles\Eclipse Adoptium\jdk-*\bin\java.exe",
    "$env:ProgramFiles\Microsoft\jdk-*\bin\java.exe",
    "$env:USERPROFILE\.jdks\*\bin\java.exe"
)

$javaFound = $null
foreach ($pattern in $javaPaths) {
    $found = Get-ChildItem -Path $pattern -ErrorAction SilentlyContinue | Select-Object -First 1
    if ($found) {
        $javaFound = $found.FullName
        Write-Host "✅ Java trouvé: $javaFound" -ForegroundColor Green
        break
    }
}

if ($javaFound) {
    # Essayer de compiler et lancer avec ce Java
    Write-Host ""
    Write-Host "[3/4] Compilation avec Java trouvé..." -ForegroundColor Yellow
    $javaDir = Split-Path $javaFound
    $javacPath = Join-Path (Split-Path $javaDir) "bin\javac.exe"
    
    if (Test-Path $javacPath) {
        Write-Host "✅ Compilateur Java trouvé" -ForegroundColor Green
        # Pour lancer, on aurait besoin des dépendances Maven dans le classpath
        Write-Host "⚠️  Nécessite Maven pour gérer les dépendances" -ForegroundColor Yellow
    }
}

# Méthode 3: Vérifier si IntelliJ peut lancer
Write-Host ""
Write-Host "[4/4] Instructions pour IntelliJ IDEA..." -ForegroundColor Yellow
Write-Host ""
Write-Host "════════════════════════════════════════" -ForegroundColor Cyan
Write-Host "  SOLUTION RECOMMANDÉE" -ForegroundColor Cyan
Write-Host "════════════════════════════════════════" -ForegroundColor Cyan
Write-Host ""
Write-Host "1. Ouvrez IntelliJ IDEA" -ForegroundColor White
Write-Host "2. Ouvrez le fichier: src/main/java/com/parkingmanager/MainApp.java" -ForegroundColor White
Write-Host "3. Clic droit sur 'MainApp' → Run 'MainApp.main()'" -ForegroundColor White
Write-Host "   OU appuyez sur Shift+F10" -ForegroundColor White
Write-Host ""
Write-Host "IntelliJ utilisera automatiquement son JDK intégré!" -ForegroundColor Green
Write-Host ""

# Méthode 4: Essayer avec les classes déjà compilées (si Java est trouvé)
if ($javaFound -and (Test-Path "target\classes\com\parkingmanager\MainApp.class")) {
    Write-Host ""
    Write-Host "⚠️  Les classes sont compilées mais les dépendances JavaFX" -ForegroundColor Yellow
    Write-Host "   doivent être dans le classpath pour lancer l'application." -ForegroundColor Yellow
    Write-Host "   Utilisez IntelliJ ou installez Maven pour gérer les dépendances." -ForegroundColor Yellow
}

Write-Host ""
Write-Host "════════════════════════════════════════" -ForegroundColor Cyan
Write-Host "Pour installer Java et Maven:" -ForegroundColor Yellow
Write-Host "  Java: https://adoptium.net/" -ForegroundColor White
Write-Host "  Maven: https://maven.apache.org/download.cgi" -ForegroundColor White
Write-Host "════════════════════════════════════════" -ForegroundColor Cyan

