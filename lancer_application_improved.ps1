# Script PowerShell amélioré pour lancer ParkingManager
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  ParkingManager - Lancement" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Fonction pour trouver Java
function Find-Java {
    $javaPaths = @(
        "$env:JAVA_HOME\bin\java.exe",
        "C:\Program Files\Java\*\bin\java.exe",
        "C:\Program Files (x86)\Java\*\bin\java.exe",
        "$env:LOCALAPPDATA\Programs\Eclipse Adoptium\*\bin\java.exe",
        "$env:ProgramFiles\Eclipse Adoptium\*\bin\java.exe",
        "$env:LOCALAPPDATA\Programs\Microsoft\*\bin\java.exe"
    )
    
    foreach ($path in $javaPaths) {
        $found = Get-ChildItem -Path $path -ErrorAction SilentlyContinue | Select-Object -First 1
        if ($found) {
            return $found.FullName
        }
    }
    
    # Essayer via where.exe
    $whereJava = where.exe java 2>$null
    if ($whereJava) {
        return $whereJava
    }
    
    return $null
}

# Fonction pour trouver Maven
function Find-Maven {
    $mavenPaths = @(
        "$env:MAVEN_HOME\bin\mvn.cmd",
        "$env:MAVEN_HOME\bin\mvn.bat",
        "C:\Program Files\Apache\maven\*\bin\mvn.cmd",
        "C:\apache-maven-*\bin\mvn.cmd",
        "$env:USERPROFILE\.m2\wrapper\maven-wrapper.jar"
    )
    
    foreach ($path in $mavenPaths) {
        $found = Get-ChildItem -Path $path -ErrorAction SilentlyContinue | Select-Object -First 1
        if ($found) {
            return $found.FullName
        }
    }
    
    # Essayer via where.exe
    $whereMvn = where.exe mvn 2>$null
    if ($whereMvn) {
        return $whereMvn
    }
    
    return $null
}

# Chercher Java
Write-Host "Recherche de Java..." -ForegroundColor Yellow
$javaPath = Find-Java
if (-not $javaPath) {
    Write-Host "❌ ERREUR: Java n'est pas installé ou n'est pas dans le PATH" -ForegroundColor Red
    Write-Host ""
    Write-Host "Solutions possibles:" -ForegroundColor Yellow
    Write-Host "1. Installez Java JDK 21 depuis https://adoptium.net/" -ForegroundColor White
    Write-Host "2. Ajoutez Java au PATH système" -ForegroundColor White
    Write-Host "3. Utilisez IntelliJ IDEA pour lancer le projet directement" -ForegroundColor White
    Write-Host ""
    Read-Host "Appuyez sur Entrée pour quitter"
    exit 1
} else {
    Write-Host "✅ Java trouvé: $javaPath" -ForegroundColor Green
    $javaVersion = & $javaPath -version 2>&1 | Select-Object -First 1
    Write-Host "   Version: $javaVersion" -ForegroundColor Gray
}

# Chercher Maven
Write-Host ""
Write-Host "Recherche de Maven..." -ForegroundColor Yellow
$mavenPath = Find-Maven
if (-not $mavenPath) {
    Write-Host "❌ ERREUR: Maven n'est pas installé ou n'est pas dans le PATH" -ForegroundColor Red
    Write-Host ""
    Write-Host "Solutions possibles:" -ForegroundColor Yellow
    Write-Host "1. Installez Maven depuis https://maven.apache.org/download.cgi" -ForegroundColor White
    Write-Host "2. Ajoutez Maven au PATH système" -ForegroundColor White
    Write-Host "3. Utilisez IntelliJ IDEA (Maven est inclus)" -ForegroundColor White
    Write-Host ""
    Read-Host "Appuyez sur Entrée pour quitter"
    exit 1
} else {
    Write-Host "✅ Maven trouvé: $mavenPath" -ForegroundColor Green
}

# Lancer l'application
Write-Host ""
Write-Host "Compilation et lancement de l'application..." -ForegroundColor Yellow
Write-Host ""

try {
    & $mavenPath clean javafx:run
    if ($LASTEXITCODE -ne 0) {
        Write-Host ""
        Write-Host "❌ ERREUR lors du lancement" -ForegroundColor Red
        Write-Host "Vérifiez:" -ForegroundColor Yellow
        Write-Host "1. Que toutes les dépendances sont téléchargées (mvn clean install)" -ForegroundColor White
        Write-Host "2. Que la base de données H2 fonctionne (base en mémoire, pas besoin de configuration)" -ForegroundColor White
        Write-Host ""
        Read-Host "Appuyez sur Entrée pour quitter"
    }
} catch {
    Write-Host ""
    Write-Host "❌ Erreur: $_" -ForegroundColor Red
    Read-Host "Appuyez sur Entrée pour quitter"
    exit 1
}

