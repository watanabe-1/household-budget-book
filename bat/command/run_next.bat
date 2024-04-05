rem @echo off
rem SetLocal EnableDelayedExpansion

:: 文字コードをUTF-8に変更
chcp 65001

:: カレントディレクトリを、現在実行しているファイルのあるフォルダへと移動させる
cd /d %~dp0

cd ../../study/

:: nextjs専用サーバの起動（新しいウィンドウで）
start "Nextjs" cmd /c "chcp 65001 && gradlew :frontend:runDevNext"