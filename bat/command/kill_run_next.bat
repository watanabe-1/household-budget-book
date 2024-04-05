rem @echo off
setlocal EnableDelayedExpansion

:: 文字コードをUTF-8に変更
chcp 65001

:: リッスンしているポートからPIDを取得（3000ポートを使用している場合）
for /f "tokens=5" %%a in ('netstat -aon ^| findstr :3000') do (
    set PID=%%a
    goto killProcess
)

:killProcess
if not defined PID (
    echo No process found listening on the specified port.
    goto end
)

:: 取得したPIDを使用してプロセスを終了
echo Found process with PID !PID! - Attempting to kill.
taskkill /PID !PID! /F

:end
endlocal