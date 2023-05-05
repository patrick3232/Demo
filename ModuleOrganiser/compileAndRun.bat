@echo off
set rootDirectory=%CD%

cd %CD%\source
xcopy /I /Y resources %rootDirectory%\classes\resources
CALL compile.bat

cd %rootDirectory%\classes
CALL run.bat
