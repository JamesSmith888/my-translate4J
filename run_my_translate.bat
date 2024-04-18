@echo off
set "JAVA_EXEC=openjdk-21.0.1\bin\java.exe"
cd /d "%~dp0" 
set "JAR_PATH=target\my-translate4J-0.0.1-SNAPSHOT.jar"
"%JAVA_EXEC%" -jar "%JAR_PATH%"