@echo off
setlocal EnableDelayedExpansion
cd /d "%~dp0"
if not exist "build\classes" mkdir "build\classes"
for /r src %%f in (*.java) do (
    set "archivo=%%f"
    echo "!archivo:\=/!"
) > fileList.txt
javac --release 21 -encoding UTF-8 -cp "lib\*" -d build/classes @fileList.txt
exit /b %errorlevel%
