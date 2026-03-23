@ECHO OFF
SETLOCAL

SET "BASE_DIR=%~dp0"
SET "WRAPPER_DIR=%BASE_DIR%.mvn\wrapper"
SET "MAVEN_VERSION=3.9.9"
SET "MAVEN_BASE=apache-maven-%MAVEN_VERSION%"
SET "MAVEN_HOME=%WRAPPER_DIR%\%MAVEN_BASE%"
SET "MAVEN_ARCHIVE=%WRAPPER_DIR%\%MAVEN_BASE%-bin.zip"
SET "DOWNLOAD_URL=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/%MAVEN_VERSION%/%MAVEN_BASE%-bin.zip"

IF EXIST "%MAVEN_HOME%\bin\mvn.cmd" GOTO run

IF NOT EXIST "%WRAPPER_DIR%" MKDIR "%WRAPPER_DIR%"

powershell -NoProfile -ExecutionPolicy Bypass -Command ^
  "$ErrorActionPreference = 'Stop';" ^
  "$zip = '%MAVEN_ARCHIVE%';" ^
  "$destination = '%WRAPPER_DIR%';" ^
  "$downloadUrl = '%DOWNLOAD_URL%';" ^
  "if (-not (Test-Path $zip)) { Invoke-WebRequest -Uri $downloadUrl -OutFile $zip; }" ^
  "Expand-Archive -Path $zip -DestinationPath $destination -Force"
IF ERRORLEVEL 1 EXIT /B %ERRORLEVEL%

:run
CALL "%MAVEN_HOME%\bin\mvn.cmd" %*
