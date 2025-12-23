@echo off
chcp 65001 >nul
echo ========================================
echo   LANCEMENT PARKING MANAGER
echo ========================================
echo.

set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17.0.17.10-hotspot
set PATH=%JAVA_HOME%\bin;%PATH%

echo Java: %JAVA_HOME%
echo.

REM Chercher Maven
where mvn >nul 2>nul
if %ERRORLEVEL% EQU 0 (
    echo Maven trouve!
    echo.
    echo Lancement avec Maven javafx:run...
    echo.
    mvn javafx:run
    goto :end
)

REM Chercher Maven Wrapper
if exist mvnw.cmd (
    echo Maven Wrapper trouve!
    echo.
    echo Lancement avec Maven Wrapper...
    echo.
    call mvnw.cmd javafx:run
    goto :end
)

echo.
echo ERREUR: Maven non trouve!
echo.
echo Utilisez IntelliJ IDEA:
echo   Maven ^> Plugins ^> javafx ^> javafx:run
echo.
pause
exit /b 1

:end
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo Erreur lors du lancement.
    pause
)

