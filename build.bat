@echo off
mkdir artifacts
dir /s /B src\*.java > artifacts\sources.txt
javac -target 1.6 -source 1.6 @artifacts\sources.txt
