rem @echo off

rem 文字コードをUTF-8に変更
chcp 65001

rem カレントディレクトリを、現在実行しているファイルのあるフォルダへと移動させる
cd /d %~dp0

REM 現在時刻を設定
set year=%date:~0,4%
set month=%date:~5,2%
set day=%date:~8,2%
set hour=%time:~0,2%
set minute=%time:~3,2%
set second=%time:~6,2%

REM ログファイルの出力パスを設定
set LOG_FILE=./logs/%year%-%month%-%day%_%hour%-%minute%-%second%.log

echo ------------------------ >> %LOG_FILE%
echo execute_sql.bat start >> %LOG_FILE%
echo %date% %time% >> %LOG_FILE%
echo ------------------------ >> %LOG_FILE%

REM db接続情報を読み込むためのファイルパスを設定
set DB_CONFIG_FILE=./config/db_config.txt

REM ファイルの存在を確認
if not exist "%DB_CONFIG_FILE%" (
    echo Configuration file %DB_CONFIG_FILE% not found. Exiting... >> %LOG_FILE%
    exit /b
)

REM ファイルからdb接続情報を読み込みます
for /f "usebackq delims=" %%x in ("%DB_CONFIG_FILE%") do (
    set "%%x"
)

REM データベースに接続できるか確認
psql -U %PGUSER% -d %PGDATABASE% -p %PGPORT% -h %PGHOST% -c "SELECT 1;" 
if %errorlevel% neq 0 (
    echo Failed to connect to the database. >> %LOG_FILE%
    exit /b
)

REM SQLファイルリストを読み込むためのファイルパスを設定
set SQL_LIST_FILE=./config/sql_list.txt

REM ファイルの存在を確認
if not exist "%SQL_LIST_FILE%" (
    echo SQL file list %SQL_LIST_FILE% not found. Exiting...>> %LOG_FILE%
    exit /b
)

REM SQLファイルリストを読み込みます
for /f %%x in (%SQL_LIST_FILE%) do (
    echo ------------------------ >> %LOG_FILE%
    echo Executing SQL file: "%%x" >> %LOG_FILE%
    echo %date% %time% >> %LOG_FILE%
    psql -U %PGUSER% -d %PGDATABASE% -p %PGPORT% -h %PGHOST% -f "%%x" >> %LOG_FILE% 2>&1
    echo ------------------------ >> %LOG_FILE%
)

echo SQL file execution completed.>> %LOG_FILE%

echo ------------------------ >> %LOG_FILE%
echo execute_sql.bat end >> %LOG_FILE%
echo %date% %time% >> %LOG_FILE%
echo ------------------------ >> %LOG_FILE%

echo Execution completed. Check %LOG_FILE% for details.

