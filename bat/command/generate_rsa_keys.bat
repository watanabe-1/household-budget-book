:: 文字コードをUTF-8に変更
chcp 65001

:: カレントディレクトリを、現在実行しているファイルのあるフォルダへと移動させる
cd /d %~dp0

:: resources\config配下に移動する
cd ../../study/backend/src/main/resources/config/certs

:: rsa key pair の作成
openssl genrsa -out keypair.pem 2048

:: public key の生成
openssl rsa -in keypair.pem -pubout -out public.pem

:: private key を PKCS#8 形式で生成
openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in keypair.pem -out private.pem