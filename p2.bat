@echo off
cd /d "%~dp0"
if not exist bin\cito\app\ProgramaP2.class (
    if not exist bin mkdir bin
    javac -encoding UTF-8 -d bin -sourcepath src src\cito\app\ProgramaP1.java src\cito\app\ProgramaP2.java
)
java -cp bin cito.app.ProgramaP2
