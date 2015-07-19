@echo off
@del /Q dist\*
@del /Q build\libs\*
call Forge\gradlew.bat build
@mkdir dist
@copy build\libs\* dist
set /p altDest=<modinstance.txt
@copy dist %altDest%