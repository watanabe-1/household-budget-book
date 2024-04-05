rem @echo off
rem SetLocal EnableDelayedExpansion

:: 文字コードをUTF-8に変更
chcp 65001

:: カレントディレクトリを、現在実行しているファイルのあるフォルダへと移動させる
cd /d %~dp0

cd ../../study/

:: クリーンの実行
gradlew :backend:cleanGen && gradlew :backend:clean