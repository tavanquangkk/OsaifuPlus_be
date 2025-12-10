# OsaifuPlus Backend 💰

<div align="center">

**日本での生活を支援する個人財務管理プラットフォームのバックエンドAPI**

[![Quarkus](https://img.shields.io/badge/Quarkus-3.29.0-blue)](https://quarkus.io/)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.20-purple)](https://kotlinlang.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-blue)](https://www.postgresql.org/)
[![License](https://img.shields.io/badge/license-MIT-green)](LICENSE)

</div>

---

## 📖 概要

OsaifuPlus Backendは、日本在住者向けの個人財務管理および生活情報共有プラットフォーム「OsaifuPlus」のサーバーサイドアプリケーションです。

### 主な機能

🔐 **認証・認可**
- JWT（JSON Web Token）ベースの認証システム
- ユーザー登録・ログイン
- 管理者認証機能

💰 **財務管理**
- 収入・支出の取引記録
- カテゴリ別の支出分析
- 月次レポート生成

👥 **ユーザー管理**
- プロフィール管理
- アカウント設定

🌏 **Live at Japan モジュール**（開発予定）
- 日本での生活情報共有
- コミュニティ投稿機能
- コメント・いいね機能

---

## 🛠 技術スタック

| カテゴリ | 技術 |
|---------|------|
| **フレームワーク** | Quarkus 3.29.0 (Supersonic Subatomic Java Framework) |
| **言語** | Kotlin 2.2.20 |
| **データベース** | PostgreSQL 17 |
| **ORM** | Hibernate ORM with Panache |
| **認証** | SmallRye JWT (RSA署名) |
| **セキュリティ** | Bcrypt パスワードハッシュ化 |
| **シリアライゼーション** | Kotlinx Serialization |
| **ビルドツール** | Maven 3.8+ |

---

## 📁 プロジェクト構造

```
OsaifuPlus_be/
├── src/
│   ├── main/
│   │   ├── kotlin/jp/tvq/osaifuplus/
│   │   │   ├── domain/              # 📄 エンティティ層
│   │   │   │   ├── User.kt
│   │   │   │   └── Transaction.kt
│   │   │   │
│   │   │   ├── repository/          # 🗄️ データアクセス層
│   │   │   │   └── UserRepository.kt
│   │   │   │
│   │   │   ├── service/             # 🧠 ビジネスロジック層
│   │   │   │   ├── auth/            # 認証サービス
│   │   │   │   ├── jwt/             # JWT トークンサービス
│   │   │   │   ├── transaction/     # 取引サービス
│   │   │   │   └── user/            # ユーザーサービス
│   │   │   │
│   │   │   ├── resource/            # 📡 REST API エンドポイント
│   │   │   │   ├── AuthResource.kt
│   │   │   │   ├── AdminAuthResource.kt
│   │   │   │   ├── TransactionResource.kt
│   │   │   │   └── UserResource.kt
│   │   │   │
│   │   │   ├── dto/                 # 📬 データ転送オブジェクト
│   │   │   │   └── AuthDTOs.kt
│   │   │   │
│   │   │   └── utils/               # 🛠️ ユーティリティ
│   │   │       └── ApiResponse.kt
│   │   │
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── import.sql
│   │       └── keys/                # 🔑 JWT 鍵ペア（開発用のみ）
│   │           ├── rsaPrivateKey.pem
│   │           └── rsaPublicKey.pem
│   │
│   └── test/kotlin/                 # 🧪 テストコード
│
├── pom.xml                          # Maven設定
├── mvnw / mvnw.cmd                  # Maven Wrapper
├── .gitignore                       # Git除外設定（本番用鍵を保護）
└── README.md
```

> ⚠️ **注意**: `keys/` ディレクトリ内のRSA鍵ペアは**開発・テスト環境専用**です。  
> 本番環境では必ず新しい鍵ペアを生成し、秘密鍵をリポジトリにコミットしないでください。

---

## 🚀 セットアップ & 起動方法

### 前提条件

以下のソフトウェアがインストールされている必要があります：

- **JDK 21** 以上
- **Maven 3.8+** (または同梱のMaven Wrapperを使用)
- **PostgreSQL 17** (ローカル環境の場合)

### 1. データベースのセットアップ

PostgreSQLを起動し、データベースとユーザーを作成します：

```bash
# PostgreSQLの起動 (Homebrewの場合)
brew services start postgresql@17

# データベースとユーザーの作成
psql -U postgres
```

```sql
CREATE DATABASE osaifu;
CREATE USER osaifu WITH PASSWORD 'osaifu';
GRANT ALL PRIVILEGES ON DATABASE osaifu TO osaifu;
\q
```

### 2. RSA鍵ペアの生成

⚠️ **セキュリティ上の重要な注意事項**

このリポジトリには開発用のRSA鍵ペアが含まれていますが、**本番環境では絶対に使用しないでください**。
本番環境では必ず新しい鍵ペアを生成し、秘密鍵をGitにコミットしないでください。

#### macOS / Linux での鍵ペア生成

```bash
# keysディレクトリの作成
mkdir -p src/main/resources/keys

# 秘密鍵の生成（2048ビットRSA）
openssl genrsa -out src/main/resources/keys/rsaPrivateKey.pem 2048

# 公開鍵の抽出
openssl rsa -in src/main/resources/keys/rsaPrivateKey.pem \
  -pubout -out src/main/resources/keys/rsaPublicKey.pem

# PKCS#8形式の秘密鍵を生成（Quarkus用）
openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt \
  -in src/main/resources/keys/rsaPrivateKey.pem \
  -out src/main/resources/keys/privateKey.pkcs8.pem
```

#### Windows (PowerShell) での鍵ペア生成

OpenSSLをインストールしていない場合は、[Git for Windows](https://git-scm-downloads.com/)に含まれるOpenSSLを使用するか、[Win32 OpenSSL](https://slproweb.com/products/Win32OpenSSL.html)をインストールしてください。

```powershell
# keysディレクトリの作成
New-Item -ItemType Directory -Force -Path src\main\resources\keys

# 秘密鍵の生成
openssl genrsa -out src\main\resources\keys\rsaPrivateKey.pem 2048

# 公開鍵の抽出
openssl rsa -in src\main\resources\keys\rsaPrivateKey.pem `
  -pubout -out src\main\resources\keys\rsaPublicKey.pem

# PKCS#8形式の秘密鍵を生成
openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt `
  -in src\main\resources\keys\rsaPrivateKey.pem `
  -out src\main\resources\keys\privateKey.pkcs8.pem
```

#### .gitignoreへの追加（推奨）

本番環境用の鍵を保護するため、`.gitignore`に以下を追加してください：

```gitignore
# JWT Keys (本番環境用)
src/main/resources/keys/*.pem
!src/main/resources/keys/.gitkeep
```

### 3. アプリケーション設定

`src/main/resources/application.properties` を確認・編集します：

```properties
# データベース設定
quarkus.datasource.db-kind=postgresql
quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/osaifu
quarkus.datasource.username=osaifu
quarkus.datasource.password=osaifu

# Hibernate設定 (開発時は update, 本番環境では validate 推奨)
quarkus.hibernate-orm.database.generation=update

# JWT設定
smallrye.jwt.sign.key.location=classpath:keys/rsaPrivateKey.pem
mp.jwt.verify.publickey.location=classpath:keys/rsaPublicKey.pem
mp.jwt.verify.issuer=https://osaifuplus.tvq.jp
```

### 4. 開発モードでの起動

```bash
# Maven Wrapperを使用（推奨）
./mvnw quarkus:dev

# またはMavenコマンド
mvn quarkus:dev
```

アプリケーションは **http://localhost:8080** で起動します。

### 5. Dev UI の利用

開発モード時は、Quarkus Dev UI が利用できます：
- **URL**: http://localhost:8080/q/dev/
- データソース、エンドポイント、設定などを確認可能

---

## 📡 API エンドポイント

### 認証 API

| メソッド | エンドポイント | 説明 | 認証 |
|---------|--------------|------|------|
| POST | `/api/v1/auth/register` | ユーザー登録 | 不要 |
| POST | `/api/v1/auth/login` | ログイン | 不要 |

**リクエスト例 (登録)**:
```json
{
  "email": "user@example.com",
  "username": "username",
  "password": "securePassword123"
}
```

**レスポンス例**:
```json
{
  "status": "success",
  "message": "作成に成功しました",
  "data": {
    "accessToken": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
    "email": "user@example.com",
    "name": "username"
  }
}
```

### 取引 API

| メソッド | エンドポイント | 説明 | 認証 |
|---------|--------------|------|------|
| POST | `/api/v1/transactions` | 取引作成 | 必要 |
| GET | `/api/v1/transactions` | 取引一覧取得 | 必要 |
| GET | `/api/v1/transactions/{id}` | 取引詳細取得 | 必要 |
| PUT | `/api/v1/transactions/{id}` | 取引更新 | 必要 |
| DELETE | `/api/v1/transactions/{id}` | 取引削除 | 必要 |

### ユーザー API

| メソッド | エンドポイント | 説明 | 認証 |
|---------|--------------|------|------|
| GET | `/api/v1/users/me` | 現在のユーザー情報取得 | 必要 |
| PUT | `/api/v1/users/me` | ユーザー情報更新 | 必要 |

### 管理者 API

| メソッド | エンドポイント | 説明 | 認証 |
|---------|--------------|------|------|
| POST | `/api/v1/admin/auth/login` | 管理者ログイン | 不要 |
| GET | `/api/v1/admin/*` | 管理者機能 | 管理者権限必要 |

---

## 🔧 ビルド & デプロイ

### 本番用パッケージング

```bash
# JARファイルの生成
./mvnw package

# Uber-JARの生成（全依存関係を含む）
./mvnw package -Dquarkus.package.jar.type=uber-jar

# 実行
java -jar target/quarkus-app/quarkus-run.jar
```

### ネイティブイメージのビルド

```bash
# GraalVMを使用してネイティブ実行可能ファイルを生成
./mvnw package -Dnative

# Dockerコンテナ内でビルド（GraalVM不要）
./mvnw package -Dnative -Dquarkus.native.container-build=true

# 実行
./target/code-with-quarkus-1.0.0-SNAPSHOT-runner
```

---

## 🧪 テスト

```bash
# 全テストの実行
./mvnw test

# 統合テストの実行
./mvnw verify
```

---

## 🗄️ データベーススキーマ

### 主要テーブル

#### `users` - ユーザー情報
```sql
- user_id (PK)
- email (UNIQUE)
- username
- password (ハッシュ化)
- created_at
- updated_at
```

#### `transactions` - 取引記録
```sql
- transaction_id (PK)
- user_id (FK)
- category_id (FK)
- amount
- type (INCOME/EXPENSE)
- description
- transaction_date
- created_at
```

---

## 🔐 セキュリティ

### 認証・認可

- **パスワード**: Bcryptでハッシュ化して保存
- **認証**: JWT（RS256署名）を使用
- **トークン有効期限**:
  - Access Token: 60分
  - Refresh Token: 30日間

### RSA鍵の管理

⚠️ **重要なセキュリティガイドライン**

1. **開発環境**
   - リポジトリに含まれる鍵ペアは開発・テスト用のみ
   - チーム開発の場合は共有しても問題なし

2. **本番環境**
   - **必ず新しい鍵ペアを生成**してください
   - 秘密鍵（`rsaPrivateKey.pem`）は絶対にGitにコミットしない
   - 秘密鍵は環境変数やシークレット管理サービス（AWS Secrets Manager、Azure Key Vault等）で管理
   - 鍵のローテーション計画を策定（推奨：3〜6ヶ月ごと）

3. **推奨される本番環境の設定**
   ```properties
   # 環境変数から読み込む
   smallrye.jwt.sign.key.location=${JWT_PRIVATE_KEY_PATH}
   mp.jwt.verify.publickey.location=${JWT_PUBLIC_KEY_PATH}
   ```

4. **鍵の保護**
   - ファイルパーミッション: `chmod 600 rsaPrivateKey.pem`（読み取り専用、所有者のみ）
   - バックアップは暗号化して保存
   - アクセスログの監視

---

## 🐳 Docker での起動

```bash
# Dockerイメージのビルド
docker build -f src/main/docker/Dockerfile.jvm -t osaifuplus-backend:latest .

# コンテナの起動
docker run -i --rm -p 8080:8080 osaifuplus-backend:latest
```

---

## 📚 参考リンク

- [Quarkus Documentation](https://quarkus.io/)
- [Kotlin Documentation](https://kotlinlang.org/docs/home.html)
- [Hibernate ORM with Panache](https://quarkus.io/guides/hibernate-orm-panache)
- [SmallRye JWT](https://quarkus.io/guides/security-jwt)

---

## 👨‍💻 開発者

**Tavan Quang** - [@tavanquangkk](https://github.com/tavanquangkk)

---

## 📄 ライセンス

This project is licensed under the MIT License.

---

## 🤝 貢献

プルリクエストを歓迎します！大きな変更の場合は、まずissueを開いて変更内容を議論してください。

---

## 🔄 今後の開発予定

- [ ] カテゴリ管理API
- [ ] 月次レポート機能
- [ ] 予算管理機能
- [ ] Live at Japan コミュニティモジュール
- [ ] 投稿・コメント機能
- [ ] タグ検索機能
- [ ] 管理者ダッシュボード

---

## ❓ トラブルシューティング

### Q: アプリケーションが起動しない

**A**: 以下を確認してください：
- PostgreSQLが起動しているか
- データベース接続情報が正しいか
- JDK 21以上がインストールされているか
- ポート8080が使用可能か

### Q: JWT認証エラーが発生する

**A**: 以下を確認してください：
- RSA鍵ペアが正しく生成されているか
- `application.properties`の鍵のパスが正しいか
- トークンの有効期限が切れていないか

### Q: データベース接続エラー

**A**: 以下を確認してください：
```bash
# PostgreSQLの状態確認
brew services list | grep postgresql

# PostgreSQLの起動
brew services start postgresql@17

# データベースの存在確認
psql -U postgres -c "\l"
```
