@echo off
cd /d "%~dp0"
if not exist bin mkdir bin
javac -encoding UTF-8 -d bin -sourcepath src src\cito\app\ProgramaP1.java src\cito\app\ProgramaP2.java
if %errorlevel%==0 (
    echo Compilado com sucesso na pasta bin.
) else (
    echo Erro na compilacao.
)
pause
