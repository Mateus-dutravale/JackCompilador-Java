@echo off
set "tools=..\nand2tetris\tools\TextComparer.bat"
set "ArquivoOriginal=..\nand2tetris\projects\10"
set "Resultados=."

echo ======================================================
echo           VALIDADOR AUTOMATICO - PROJETO 10
echo ======================================================

echo.
echo [1/3] TESTANDO: ArrayTest
call "%tools%" "%ArquivoOriginal%\ArrayTest\MainT.xml" "%Resultados%\ArrayTest\MainT.xml"

echo.
echo [2/3] TESTANDO: Square (3 arquivos)
call "%tools%" "%ArquivoOriginal%\Square\MainT.xml" "%Resultados%\SquareTest\MainT.xml"
call "%tools%" "%ArquivoOriginal%\Square\SquareT.xml" "%Resultados%\SquareTest\SquareT.xml"
call "%tools%" "%ArquivoOriginal%\Square\SquareGameT.xml" "%Resultados%\SquareTest\SquareGameT.xml"

echo.
echo [3/3] TESTANDO: ExpressionLessSquare (3 arquivos)
call "%tools%" "%ArquivoOriginal%\ExpressionLessSquare\MainT.xml" "%Resultados%\ExpressionLessSquareTest\MainT.xml"
call "%tools%" "%ArquivoOriginal%\ExpressionLessSquare\SquareT.xml" "%Resultados%\ExpressionLessSquareTest\SquareT.xml"
call "%tools%" "%ArquivoOriginal%\ExpressionLessSquare\SquareGameT.xml" "%Resultados%\ExpressionLessSquareTest\SquareGameT.xml"

echo.
echo ======================================================
echo           TESTES FINALIZADOS
echo ======================================================
pause