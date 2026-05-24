@echo off
echo ======================================================
echo      COMPILANDO O CODIGO JAVA (GERANDO .CLASS)
echo ======================================================

javac src\Main\java\*.java

set "JAVA_CMD=java -cp src Main.java.AnalisadorJack"
set "TEST_DIR=test\11_test"

echo.
echo ======================================================
echo      GERANDO ARQUIVOS VM (FASE 11)
echo ======================================================

echo.
echo [TESTE COMPARAÇÃO] NAND2TETRIS_PONG...
%JAVA_CMD% "nand2tetris\projects\11\Pong"

echo.
echo [TESTE 1] Seven...
%JAVA_CMD% "%TEST_DIR%\Seven"

echo.
echo [TESTE 2] Square...
%JAVA_CMD% "%TEST_DIR%\Square"

echo.
echo [TESTE 3] Average...
%JAVA_CMD% "%TEST_DIR%\Average"

echo.
echo [TESTE 4] ConvertToBin...
%JAVA_CMD% "%TEST_DIR%\ConvertToBin"

echo.
echo [TESTE 5] ComplexArrays...
%JAVA_CMD% "%TEST_DIR%\ComplexArrays"

echo.
echo [TESTE 6] Pong...
%JAVA_CMD% "%TEST_DIR%\Pong"

echo.
echo ======================================================
echo  COMPILACAO FINALIZADA!
echo ======================================================
pause