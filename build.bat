@echo off
if exist out rmdir /s /q out
javac -d out src\juanparser\*.java
if errorlevel 1 exit /b 1
jar cf juanparser.jar -C out .
copy /y juanparser.jar C:\Users\USER\Desktop\Pixleboxed\lib\juanparser.jar