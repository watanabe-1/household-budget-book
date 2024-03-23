# household-budget-book

## Kotlin spring framework の勉強用に自分用家計簿の作成

本プロジェクトは右記リポジトリ[book-git-repository](https://github.com/watanabe-1/book-git-repository)
をもとに家計簿アプリを作成しています。
また下記内容を参考に作成しております。

家計簿の表示内容は楽天家計簿を参考に作成 →[楽天家計簿](https://support.rakuten-card.jp/faq/show/127262?category_id=886&return_path=%2Fcategory%2Fshow%2F886%3Fpage%3D1%26site_domain%3Dguest%26site_domain%3Dguest%26sort%3Dsort_access%26sort_order%3Ddesc&site_domain=guest)

- 家計簿データの取り込み csv 形式も楽天家計簿で出力される形式に合わせている

本プロジェクトでは 主に下記内容を使用しています。

- Kotlin のフレームワークとして、[Spring Boot](https://spring.io/projects/spring-boot)使用
- Kotlin のビルドツールとして[Gradle](https://gradle.org/)を使用
- JavaScript のパッケージ管理システムとして node.js 、 npm を使用 → [node.js](https://nodejs.org/ja/)
    - gradel から利用するため node.js をインストールしていなくても実行可能
- db は PostgreSQL を使用 → [PostgreSQL](https://www.enterprisedb.com/downloads/postgres-postgresql-downloads)
- RSA Public & Private
  Keysの作成にはopensslを使用→ [openssl](https://slproweb.com/products/Win32OpenSSL.html)

### pull 後の流れ

1. db 環境の作成

    - PostgreSQL をインストール → [PostgreSQL](https://www.enterprisedb.com/downloads/postgres-postgresql-downloads)

   詳しいインストール方法は右記サイトなどを参考に → [PostgreSQL を Windows にインストールするには](https://qiita.com/tom-sato/items/037b8f8cb4b326710f71)
    - インストール後は下記流れで ユーザー、スキーマなどを作成
        - スーパーユーザーでログイン
            - `psql -U user_name`
        - ユーザーの作成
            - `create user user_name with password 'password' createdb`;
        - スキーマの作成
            - `create database db_name with owner user_name;`
    - household-budget-book\bat\sql\config\db_config.txt を環境に合わせて作成(下記例参照)

      ```txt:db_config.txt
       PGUSER=user_name
       PGPASSWORD=password
       PGDATABASE=db_name
       PGPORT=5432
       PGHOST=localhost
      ```

    - household-budget-book\bat\sql\execute_sql.bat を実行すると下記 sql ファイルが実行される
        - household-budget-book\db\sql\DDL\ALL\CREATE_ALL.sql
        - household-budget-book\db\sql\DML\INSERT 内の sql
    - household-budget-book\study\src\main\resources\config\properties\database.properties を環境に合わせて作成(
      下記例参照)

      ```properties:database.properties
      jdbc.driverClassName=org.postgresql.Driver
      jdbc.url=jdbc:postgresql://localhost:5432/db_name
      jdbc.username=user_name
      jdbc.password=password
      jdbc.maxTotal=10
      ```
2. RSA Public & Private Keys の作成
    - opensslがインストールされていない場合はインストール→ [openssl](https://slproweb.com/products/Win32OpenSSL.html)
    - 下記コマンドを実行してRSA Public & Private Keysを作成
      ```
      # resources\config配下に移動する。下記だとエラーになるため環境ごとに変えること
      cd book-git-repository\study\src\main\resources\config\certs
      # rsa key pair の作成
      openssl genrsa -out keypair.pem 2048
      # public key の生成
      openssl rsa -in keypair.pem -pubout -out public.pem
      # private key を PKCS#8 形式で生成
      openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in keypair.pem -out private.pem
      ```

3. ビルドからログインまで
    - お好みで household-budget-book\study\src\main\resources\static\images 内の画像を好きな画像に変更する
    - household-budget-book\study で `gradlew bootrun` コマンドを実施(もしくは
      household-budget-book\bat\commond\bootrun.bat
      を実行)することでビルドとサーバの起動が実行される
    - サーバー起動後 admin/admin でシステム管理者としてログイン可能

### 開発環境(Kotlin - Intellij IDEA)

1. プロジェクトを Intellij IDEA で開く

### デバッグ(Kotlin - Intellij IDEA)

1. サーバーを起動した状態で [実行] -> [実行構成の編集] からポート:7778 にアクセス
    - 通常のデバッグと同じくブレイクポイントなどが使用可能

### 開発環境(js(ts) - Visual Studio Code)

1. `gradlew --no-daemon watch` コマンド (もしくは household-budget-book\bat\commond\daemonwatch.bat) を起動することで js
   のファイル監視が可能

    - js のファイル監視がされている状態では js ファイルを編集、保存すると自動的にトラ
      ンスパイルされ household-budget-book\study\src\main\resources\static 配下に出力され、サーバに反映される。
    - js(ts、json) の formatter は prettier,eslint を使用して作成している(vscode
      などを使用する場合は拡張機能として導入可能)

2. プロジェクトを Visual Studio Code で開く
3. 推奨される拡張機能の install

### デバッグ(js(ts))

1. js もソースマップが出力されているため、ブラウザなどでデバッグ可能
    - react のデバッグは右記 Chrome
      拡張機能で可能 →[React Developer Tools](https://chrome.google.com/webstore/detail/react-developer-tools/fmkadmapgofadopljbjfkapdkoienihi?hl=ja)

### js(ts) のみビルドしたい場合

1. ライブラリのインストール(更新)
    - household-budget-book\study で`gradlew npmInstall`コマンドを実施
2. webpack を使用し js をビルド
    - household-budget-book\study で`gradlew webpack`コマンドを実施

### その他

- ログは household-budget-book\study\logs 配下に出力
- -DB 定義は household-budget-book\spreadsheet\DB 定義.xlsx
    - Google スプレッドシートに取り込み、household-budget-book\spreadsheet\gas 配下のマクロファイルを設定すれば create
      文生成などのマクロが使用可能
- 家計簿データの例は household-budget-book\example 配下に
    - 家計簿登録画面からそのまま登録可能
- 一度ビルドからサーバー起動まで完了している場合は、household-budget-book\bat\bootrun_and_daemonwatch.bat からサーバーの起動、js
  ファイル監視の起動をまとめて実行可能
