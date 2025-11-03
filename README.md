```bazaar
    # todo
    osaifuplus-backend/
├── pom.xml                 # 📦 Mavenプロジェクト定義
├── mvnw                    # Mavenラッパー (Linux/Mac)
├── mvnw.cmd                # Mavenラッパー (Windows)
├── .gitignore
├── .dockerignore
├── Dockerfile.jvm          # 🐳 Dockerfile (JVMモード)
├── Dockerfile.native       # 🐳 Dockerfile (Nativeモード)
│
└── src/
    ├── main/
    │   ├── kotlin/
    │   │   └── com/example/osaifuplus/   # 🔵 メインパッケージ
    │   │       │
    │   │       ├── data/                 # 📄【データ層】DBエンティティ
    │   │       │   ├── User.kt
    │   │       │   ├── Transaction.kt
    │   │       │   └── Asset.kt
    │   │       │
    │   │       ├── repository/           # 🗄️【データ層】データアクセスロジック
    │   │       │   ├── UserRepository.kt
    │   │       │   └── TransactionRepository.kt
    │   │       │
    │   │       ├── service/              # 🧠【ビジネスロジック層】
    │   │       │   ├── AuthService.kt    # (登録, ログイン, パスワードハッシュ化)
    │   │       │   ├── TokenService.kt   # (JWTトークン生成・検証)
    │   │       │   └── TransactionService.kt
    │   │       │
    │   │       ├── web/                  # 📡【API層】エンドポイント
    │   │       │   ├── AuthResource.kt   # (/auth/register, /auth/login)
    │   │       │   ├── TransactionResource.kt # (/api/transactions)
    │   │       │   └── AssetResource.kt
    │   │       │
    │   │       ├── dto/                  # 📬【API層】データ転送オブジェクト
    │   │       │   ├── AuthDto.kt        # (RegisterRequest, LoginRequest, AuthResponse)
    │   │       │   └── TransactionDto.kt
    │   │       │
    │   │       ├── config/               # ⚙️ アプリケーション設定
    │   │       │   ├── SecurityConfig.kt # (CORS, JWTフィルターなど)
    │   │       │   └── ExceptionMappers.kt # (カスタムエラーハンドリング)
    │   │       │
    │   │       └── util/                 # 🛠️ 共通ユーティリティ
    │   │           └── PasswordUtil.kt   # (Bcryptなどのラッパー)
    │   │
    │   └── resources/
    │       ├── application.properties    # 🔑 Quarkus設定ファイル
    │       └── import.sql                # (開発用) 起動時の初期データ
    │
    └── test/
        ├── kotlin/
        │   └── com/example/osaifuplus/
        │       ├── web/
        │       │   └── AuthResourceTest.kt   # 🧪 APIの統合テスト
        │       └── service/
        │           └── AuthServiceTest.kt  # 🧪 ビジネスロジックの単体テスト
        └── resources/
            └── application-test.properties # テスト用の設定 (H2 DBなど)
            
            
```



# code-with-quarkus

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/code-with-quarkus-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

## Related Guides

- REST ([guide](https://quarkus.io/guides/rest)): A Jakarta REST implementation utilizing build time processing and Vert.x. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it.
- Hibernate ORM with Panache and Kotlin ([guide](https://quarkus.io/guides/hibernate-orm-panache-kotlin)): Define your persistent model in Hibernate ORM with Panache
- REST Jackson ([guide](https://quarkus.io/guides/rest#json-serialisation)): Jackson serialization support for Quarkus REST. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it
- Kotlin ([guide](https://quarkus.io/guides/kotlin)): Write your services in Kotlin
- Hibernate ORM with Panache ([guide](https://quarkus.io/guides/hibernate-orm-panache)): Simplify your persistence code for Hibernate ORM via the active record or the repository pattern
- JDBC Driver - PostgreSQL ([guide](https://quarkus.io/guides/datasource)): Connect to the PostgreSQL database via JDBC

## Provided Code

### Hibernate ORM

Create your first JPA entity

[Related guide section...](https://quarkus.io/guides/hibernate-orm)

[Related Hibernate with Panache section...](https://quarkus.io/guides/hibernate-orm-panache)

[Related Hibernate with Panache in Kotlin section...](https://quarkus.io/guides/hibernate-orm-panache-kotlin)

### REST

Easily start your REST Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)
