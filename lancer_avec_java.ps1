# Script pour lancer avec le Java trouvé
$javaPath = "C:\Users\amine\.jdks\corretto-23.0.2\bin\java.exe"
$mavenWrapperUrl = "https://repo.maven.apache.org/maven2/io/takari/maven-wrapper/0.5.6/maven-wrapper-0.5.6.jar"

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  ParkingManager - Lancement" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "✅ Java trouvé: $javaPath" -ForegroundColor Green

# Vérifier si Maven wrapper existe
$mvnwPath = ".\mvnw.cmd"
if (-not (Test-Path $mvnwPath)) {
    Write-Host ""
    Write-Host "⚠️  Maven wrapper non trouvé. Téléchargement..." -ForegroundColor Yellow
    
    # Créer le répertoire .mvn/wrapper
    $wrapperDir = ".mvn\wrapper"
    if (-not (Test-Path $wrapperDir)) {
        New-Item -ItemType Directory -Path $wrapperDir -Force | Out-Null
    }
    
    # Télécharger Maven wrapper
    try {
        Invoke-WebRequest -Uri $mavenWrapperUrl -OutFile "$wrapperDir\maven-wrapper.jar" -ErrorAction Stop
        Write-Host "✅ Maven wrapper téléchargé" -ForegroundColor Green
    } catch {
        Write-Host "❌ Impossible de télécharger Maven wrapper" -ForegroundColor Red
        Write-Host "Essayons une autre méthode..." -ForegroundColor Yellow
    }
}

# Essayer de trouver Maven dans IntelliJ
Write-Host ""
Write-Host "Recherche de Maven dans IntelliJ..." -ForegroundColor Yellow
$ideaMavenPaths = @(
    "$env:LOCALAPPDATA\JetBrains\Toolbox\apps\IDEA-C\*\plugins\maven\lib\maven3\bin\mvn.cmd",
    "$env:ProgramFiles\JetBrains\IntelliJ IDEA *\plugins\maven\lib\maven3\bin\mvn.cmd"
)

$mavenFound = $null
foreach ($pattern in $ideaMavenPaths) {
    $found = Get-ChildItem -Path $pattern -ErrorAction SilentlyContinue | Select-Object -First 1
    if ($found) {
        $mavenFound = $found.FullName
        Write-Host "✅ Maven trouvé: $mavenFound" -ForegroundColor Green
        break
    }
}

if ($mavenFound) {
    Write-Host ""
    Write-Host "Lancement de l'application avec Maven..." -ForegroundColor Yellow
    Write-Host ""
    
    # Définir JAVA_HOME pour Maven
    $env:JAVA_HOME = "C:\Users\amine\.jdks\corretto-23.0.2"
    
    & $mavenFound clean javafx:run
    exit $LASTEXITCODE
} else {
    Write-Host ""
    Write-Host "❌ Maven non trouvé" -ForegroundColor Red
    Write-Host ""
    Write-Host "════════════════════════════════════════" -ForegroundColor Cyan
    Write-Host "  SOLUTION ALTERNATIVE" -ForegroundColor Cyan
    Write-Host "════════════════════════════════════════" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Lancez depuis IntelliJ IDEA:" -ForegroundColor Yellow
    Write-Host "1. Ouvrez: src/main/java/com/parkingmanager/MainApp.java" -ForegroundColor White
    Write-Host "2. Clic droit → Run 'MainApp.main()'" -ForegroundColor White
    Write-Host ""
    Write-Host "OU installez Maven:" -ForegroundColor Yellow
    Write-Host "  https://maven.apache.org/download.cgi" -ForegroundColor White
    Write-Host ""
}

