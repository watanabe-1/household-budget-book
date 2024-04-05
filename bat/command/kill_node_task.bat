rem @echo off
setlocal

:: 文字コードをUTF-8に変更
chcp 65001

:: Node.jsプロセスを検索し、それぞれを終了
for /f "tokens=2" %%i in ('tasklist ^| findstr /i "node.exe"') do (
    taskkill /pid %%i /f
)

endlocal
echo All Node.js tasks have been terminated.
