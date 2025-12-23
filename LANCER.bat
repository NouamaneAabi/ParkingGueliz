@echo off
chcp 65001 >nul
echo ========================================
echo   LANCEMENT PARKING MANAGER
echo ========================================
echo.

set JAVA_HOME=C:\Users\amine\.jdks\corretto-23.0.2
set PATH=%JAVA_HOME%\bin;%PATH%

echo Java: %JAVA_HOME%
echo.

if exist mvnw.cmd (
    echo Utilisation de Maven Wrapper...
    echo.
    call mvnw.cmd clean javafx:run
) else if exist mvn.cmd (
    echo Utilisation de Maven...
    echo.
    call mvn.cmd clean javafx:run
) else (
    echo Maven non trouvé.
    echo.
    echo Utilisez IntelliJ IDEA:
    echo   1. Ouvrez IntelliJ
    echo   2. Ouvrez ce projet
    echo   3. Ouvrez MainApp.java
    echo   4. Clic droit ^> Run
    echo.
    pause
)

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo Erreur lors du lancement.
    pause
)

