@echo off
chcp 65001 >nul
echo ========================================
echo   LANCEMENT DU PROJET PARKING MANAGER
echo ========================================
echo.

REM Définir JAVA_HOME
set JAVA_HOME=C:\Users\amine\.jdks\corretto-23.0.2
set PATH=%JAVA_HOME%\bin;%PATH%

echo Java trouvé: %JAVA_HOME%
echo.

REM Chercher Maven
where mvn >nul 2>nul
if %ERRORLEVEL% EQU 0 (
    echo Maven trouvé dans PATH!
    echo.
    echo Compilation et lancement...
    echo.
    mvn clean javafx:run
    goto :end
)

REM Chercher Maven dans des emplacements communs
set MAVEN_PATHS[0]=%USERPROFILE%\.m2\wrapper\maven-wrapper.jar
set MAVEN_PATHS[1]=C:\Program Files\Apache\maven\bin\mvn.cmd
set MAVEN_PATHS[2]=C:\apache-maven\bin\mvn.cmd

echo Recherche de Maven...
for %%P in ("%USERPROFILE%\.m2\wrapper\maven-wrapper.jar" "C:\Program Files\Apache\maven\bin\mvn.cmd" "C:\apache-maven\bin\mvn.cmd") do (
    if exist %%P (
        echo Maven trouvé: %%P
        set MAVEN_FOUND=%%P
        goto :found_maven
    )
)

echo.
echo ========================================
echo   MAVEN NON TROUVÉ
echo ========================================
echo.
echo Le projet nécessite Maven pour compiler et lancer.
echo.
echo OPTION 1: Utiliser IntelliJ IDEA (RECOMMANDÉ)
echo   1. Ouvrez IntelliJ IDEA
echo   2. Ouvrez ce projet
echo   3. Ouvrez: src\main\java\com\parkingmanager\MainApp.java
echo   4. Clic droit ^> Run 'MainApp.main()'
echo.
echo OPTION 2: Installer Maven
echo   Téléchargez depuis: https://maven.apache.org/download.cgi
echo   Ajoutez-le au PATH système
echo.
pause
exit /b 1

:found_maven
echo.
echo Lancement avec Maven...
echo.
mvn clean javafx:run

:end
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo Erreur lors du lancement.
    pause
)

