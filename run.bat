@echo off
title KEC Student Evaluation System (R2024)
echo Compiling...
javac -cp "lib/mysql-connector-j-8.0.33.jar" -d out src/main/java/com/kec/model/*.java src/main/java/com/kec/DBConnection.java src/main/java/com/kec/dao/*.java src/main/java/com/kec/MainApp.java
if %ERRORLEVEL% NEQ 0 (
    echo Compilation failed!
    pause
    exit /b 1
)
echo Running KEC Student Evaluation System...
java -cp "out;lib/mysql-connector-j-8.0.33.jar" com.kec.MainApp
pause
