@echo off
:: Ajuste para o caminho onde o TextComparer.bat realmente está no seu computador
set "COMPARER=nand2tetris\tools\TextComparer.bat"
set "ORIGINAIS=nand2tetris\projects\10"

echo ======================================================
echo           COMPARANDO SEU XML COM O GABARITO
echo ======================================================

echo.
echo [TESTE 1] ArrayTest...
call "%COMPARER%" "%ORIGINAIS%\ArrayTest\Main.xml" "test\10_test\ArrayTest\MainP.xml"

echo.
echo [TESTE 2] Square (Main)...
call "%COMPARER%" "%ORIGINAIS%\Square\Main.xml" "test\10_test\SquareTest\MainP.xml"

echo.
echo [TESTE 3] ExpressionLessSquare (Main)...
call "%COMPARER%" "%ORIGINAIS%\ExpressionLessSquare\Main.xml" "test\10_test\ExpressionLessSquareTest\MainP.xml"

pause