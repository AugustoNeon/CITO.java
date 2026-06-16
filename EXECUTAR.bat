@echo off
cd /d "%~dp0"
title CITO - Medicos e Pacientes

where java >nul 2>nul
if errorlevel 1 (
    echo Java nao foi encontrado no computador.
    echo Instale o JDK 17 ou superior e tente de novo.
    pause
    exit /b
)

if not exist bin\cito\app\ProgramaP2.class (
    echo Compilando o programa...
    if not exist bin mkdir bin
    javac -encoding UTF-8 -d bin -sourcepath src src\cito\app\ProgramaP1.java src\cito\app\ProgramaP2.java
    if errorlevel 1 (
        echo Erro ao compilar.
        pause
        exit /b
    )
)

if not exist dados\base.dat (
    echo Gerando a base de dados...
    java -cp bin cito.app.ProgramaP1
)

echo Abrindo o sistema...
start "" javaw -cp bin cito.app.ProgramaP2
