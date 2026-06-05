# Agent Instructions

This repository is a multi-module Gradle project for language learning. It contains separate applications for the HTTP API, Telegram bot, UI, a shared domain/data-access module, a LibreTranslate client, and a generated OpenAPI Java client.

Main rule: before making changes, identify the layer and module the task belongs to. Keep responsibilities separated. Do not mix `app-api`, `app-bot`, and `app-domain` concerns.

## Module structure

The active Gradle modules are declared in `settings.gradle.kts`:

- `app-api`
- `app-ui`
- `app-bot`
- `app-domain`
- `libretranslate-client`
- `openapi-java-client`

The root `api/` directory is not included in `settings.gradle.kts`. Do not treat it as an active module and do not change it unless explicitly requested.

## Global project rules

- The base Java version is configured in the root `build.gradle.kts`: Java 21 toolchain.
- Java backend code lives in `app-api` and `libretranslate-client`.
- Kotlin backend code lives in `app-bot` and `app-domain`.
- UI code lives under `app-ui/src` and is built with Node/Vite.
- Flyway migrations live in `app-api/src/main/resources/db/migration`.
- All new DB queries, repository methods, and data-access logic must be added to `app-domain`, both for API-related data and bot-related data.
- `app-api` and `app-bot` should call `app-domain` repositories/services for new database access instead of adding new SQL/JDBC/Exposed queries locally.
- Existing legacy repositories in `app-api` may remain unless the task is to refactor them, but do not expand them with new query logic if the change can be implemented in `app-domain`.
- If a public `app-api` contract changes, check DTOs, controllers, `openapi.yaml`, UI clients, and the generated Java client.
- If the DB schema changes, add a Flyway migration in `app-api` and synchronize mappings/repositories in `app-domain`.
- Do not add in-memory storage for bot business state. Bot state and callback payloads must be stored in the database.
- Callback buttons and bot state are independent concepts. Callback payloads are stored in `bot__callback_actions`; callback handling must not change `bot__user_states` unless explicitly required by the task.
- All tables related to the Telegram bot must use the `bot__` prefix.
- API users and bot users are separate entities. Do not merge them into one model. The link between them is stored separately in `bot__user_api_links`.

## Module `app-api`

### Purpose

`app-api` is the main Spring Boot HTTP API application. It is responsible for:

- HTTP REST API endpoints;
- registration, authentication, and JWT;
- application user management;
- word, word-pair, and group management;
- Flyway migrations for the PostgreSQL schema;
- OpenAPI specification generation;
- serving built UI static files from `app-api/src/main/resources/static`.

### Important directories

- `app-api/src/main/java/com/dp9v/lingua_learn_server/api/controller` — REST controllers.
- `app-api/src/main/java/com/dp9v/lingua_learn_server/api/service` — API business logic.
- `app-api/src/main/java/com/dp9v/lingua_learn_server/api/repositories` — existing/legacy API data access.
- `app-api/src/main/java/com/dp9v/lingua_learn_server/api/dto` — REST API DTOs.
- `app-api/src/main/resources/db/migration` — Flyway migrations.
- `app-api/src/main/resources/openapi.yaml` — OpenAPI specification used to generate `openapi-java-client`.

### Change rules

- Add new REST endpoints through controller + service layers.
- For new database queries required by an endpoint, add repository/service methods in `app-domain` and call them from `app-api`.
- Do not add new JDBC/SQL query methods directly to `app-api` repositories unless explicitly requested.
- Existing `app-api` repositories can remain as legacy code, but prefer moving new data-access behavior to `app-domain`.
- For DB schema changes, create a new migration named `VXXX__description.sql`. Do not edit already applied migrations unless the task explicitly targets local unreleased state.
- API tables use the `api__` prefix.
- Bot tables are also migrated through Flyway here, but their mappings and data-access logic must live in `app-domain`.
- After changing public API behavior, update/check `openapi.yaml` and client calls in `app-ui/src/src/api`.
- If `app-api` starts using new beans from `app-domain`, ensure `app-api` has the required Gradle dependency and component scanning still covers the package.

## Module `app-domain`

### Purpose

`app-domain` is the shared domain/data-access module. It encapsulates database mappings, DTOs, repositories, and services that are reused by applications.

All new DB queries and data-access methods belong here, for both:

- API/application data;
- Telegram bot data.

Historically, this module mainly contains bot-table support. New API-facing data-access code should also be added here instead of adding new query logic directly to `app-api`.

Current bot-related support includes Exposed tables, DTOs, and repositories for:

- `bot__users`;
- `bot__user_api_links`;
- `bot__user_states`;
- `bot__user_settings`;
- `bot__callback_actions`.

### Important directories

- `app-domain/src/main/kotlin/com/dp9v/lingua_learn_server/bot/db/tables` — Exposed table mappings for bot tables.
- `app-domain/src/main/kotlin/com/dp9v/lingua_learn_server/bot/db/dto` — DTOs used by bot/domain code.
- `app-domain/src/main/kotlin/com/dp9v/lingua_learn_server/bot/db/repository` — Spring repositories that execute Exposed transactions.

If API-related domain/data-access code is added, place it in a clear package under `app-domain/src/main/kotlin/com/dp9v/lingua_learn_server/...`, for example an `api` or application-domain package, not inside Telegram-specific packages.

### Change rules

- Add all new DB queries, repository methods, and data-access services in `app-domain`.
- `app-api` and `app-bot` may call `app-domain` repositories/services, but should not own new SQL/JDBC/Exposed query logic.
- Keep DTOs in `app-domain` independent from framework-specific API types where possible.
- Do not put Telegram SDK types into `app-domain` DTOs. Convert Telegram objects in `app-bot` before calling domain code.
- Do not add a dependency from `app-domain` to `app-bot`. The dependency direction is `app-bot -> app-domain` only.
- If API modules use domain code, the dependency direction should be `app-api -> app-domain`; do not make `app-domain` depend on `app-api`.
- Spring classes in Kotlin must be compatible with proxying. This module applies `kotlin("plugin.spring")`; for new beans use `@Repository`, `@Service`, or `@Component` and do not disable the Spring Kotlin plugin.
- If you add a new bot table:
  1. add a Flyway migration in `app-api`;
  2. add the Exposed mapping in `app-domain/.../tables`;
  3. add DTOs if needed;
  4. add repository/service methods in `app-domain`;
  5. use those methods from `app-bot`.
- If you add new API data access:
  1. add/adjust Flyway migration in `app-api` if the schema changes;
  2. add mapping/repository/service code in `app-domain`;
  3. expose the behavior to `app-api` through domain services/repositories;
  4. update API service/controller code in `app-api`;
  5. update tests and OpenAPI/UI clients when public API changes.

## Module `app-bot`

### Purpose

`app-bot` is the Spring Boot Telegram bot application. It is responsible for:

- starting the Telegram long polling bot;
- routing Telegram `Update` objects through `UpdateHandler` implementations;
- handling `/start`, `/help`, text messages, and callbacks;
- translating words through `libretranslate-client`;
- building inline buttons;
- processing callback buttons using callback payload stored in the database;
- connecting Exposed to the configured datasource through `Database.connect(dataSource)`.

### Important directories

- `app-bot/src/main/kotlin/com/dp9v/lingua_learn_server/bot` — application entry point and `LinguaLearnBot`.
- `app-bot/src/main/kotlin/com/dp9v/lingua_learn_server/bot/handlers` — Telegram update handlers.
- `app-bot/src/main/kotlin/com/dp9v/lingua_learn_server/bot/callback` — callback-data models and utilities.
- `app-bot/src/main/kotlin/com/dp9v/lingua_learn_server/bot/config` — bot and DB configuration.
- `app-bot/src/main/resources/application.yml` — bot token, datasource, and LibreTranslate settings.

### Change rules

- Do not store user state or callback payloads in maps, static fields, or other in-memory storage. Use `app-domain` and the database.
- Do not create DB-access services in `app-bot`. Put new bot DB queries and services in `app-domain`.
- `TranslateHandler` should translate and create callback buttons, but must not mix callback payloads with bot state.
- `SaveWordCallbackHandler` should process callback actions using payloads from `bot__callback_actions`. Callback buttons must not modify `bot__user_states` unless explicitly required.
- Telegram `callback_data` has a strict size limit. Store only a compact reference in the button, for example `cb:<uuid>`, and store the full payload in the database.
- When adding a new callback action:
  1. add the action enum/model in `app-bot/.../callback`;
  2. save the payload through an `app-domain` repository;
  3. put only the packed callback id into the button;
  4. in the handler, unpack the callback id and read/consume the payload through `app-domain`;
  5. add tests for successful handling, repeated click/consumed callback, and invalid/expired callback.
- When using nullable DTO properties from another module, do not rely on Kotlin smart casts. Store the value in a local variable:

```kotlin
val payload = callbackAction.payload
if (payload == null) {
    // handle missing payload
    return
}
val pending = objectMapper.readValue<PendingTranslation>(payload)
```

## Module `libretranslate-client`

### Purpose

`libretranslate-client` is a Java client library for LibreTranslate. It is responsible for:

- HTTP calls to LibreTranslate;
- translation and language-detection DTOs;
- Spring auto-configuration for the client;
- properties for `libretranslate.url`.

### Important directories

- `libretranslate-client/src/main/java/com/dp9v/libretranslate/LibreTranslateClient.java` — main client.
- `libretranslate-client/src/main/java/com/dp9v/libretranslate/dto` — DTOs and `LanguageCode`.
- `libretranslate-client/src/main/java/com/dp9v/libretranslate/config` — auto-configuration and properties.

### Change rules

- Do not put Telegram logic in this module.
- Do not put word-saving business logic in this module.
- If the LibreTranslate contract changes, update DTOs and all usages in `app-bot`.
- Keep this module reusable: it should remain a client library, not part of bot flow or API business flow.

## Module `app-ui`

### Purpose

`app-ui` is a React/Vite frontend. Its Gradle module wraps the Node build and copies the built UI into `app-api`.

### Important directories

- `app-ui/src/src/pages` — pages.
- `app-ui/src/src/components` — React components.
- `app-ui/src/src/components/ui` — base UI components.
- `app-ui/src/src/api` — HTTP clients for `app-api`.
- `app-ui/src/src/context` — React context, including auth.
- `app-ui/src/package.json` — npm scripts and dependencies.

### Change rules

- Change UI code inside `app-ui/src`.
- When API contracts change, synchronize types and API functions in `app-ui/src/src/api`.
- UI build/copy is handled by Gradle task `:app-ui:copyFrontend`; it runs npm build and copies `dist` into `app-api/src/main/resources/static`.
- Do not manually edit generated static files in `app-api/src/main/resources/static` if the change should come from React source code.

## Module `openapi-java-client`

### Purpose

`openapi-java-client` is a Java client generated from `app-api/src/main/resources/openapi.yaml` by OpenAPI Generator.

### Change rules

- Do not manually edit generated client code if the change should reflect REST API changes.
- First change API DTOs/controllers in `app-api`, update `openapi.yaml`, then regenerate the client.
- Generation configuration lives in `app-api/build.gradle.kts` in the `openApiGenerate` block.
- The module is included in `settings.gradle.kts`, but before using it from another module, check that the Gradle dependency exists.

## Root project

### Purpose

The root project defines shared Gradle configuration:

- module list in `settings.gradle.kts`;
- Java 21 toolchain;
- shared repositories;
- JUnit Platform for tests;
- forwarding `JDBC_CONNECTION_STRING` to tests.

### Change rules

- When adding a new module, add it to `settings.gradle.kts`.
- Shared plugin versions should usually live in the root `build.gradle.kts` if multiple modules use them.
- Do not change the toolchain without checking all Java/Kotlin/Spring modules.

## Database and migrations

### Where things live

- Flyway migrations: `app-api/src/main/resources/db/migration`.
- Test DB cleanup SQL: `app-api/src/test/resources/sql/drop.sql`.
- New DB queries/repositories/services: `app-domain`.
- Existing legacy API repository layer: `app-api/.../repositories`.
- Bot table Exposed mappings: `app-domain/.../tables`.
- Bot repository layer: `app-domain/.../repository`.

### Table prefixes

- `api__*` — API application tables.
- `bot__*` — Telegram bot tables.

### Current bot tables

- `bot__users` — Telegram users. `id` is the Telegram user id.
- `bot__user_api_links` — link between Telegram users and API users.
- `bot__user_states` — current state of a bot user in the bot state graph.
- `bot__user_settings` — bot user settings; columns `key` and `value`.
- `bot__callback_actions` — callback button payloads; Telegram callback data contains only a reference to this table.

### Schema change rules

- For new schema changes, create a new `VXXX__*.sql` migration.
- Synchronize migrations with `app-domain` mappings and repositories.
- If a table is used in tests, update `drop.sql` with the correct deletion order for foreign keys.
- JSON-like payloads currently use `TEXT`, unless PostgreSQL JSON mapping is explicitly configured in Exposed.
- Migrations stay in `app-api`; DB query/data-access code goes in `app-domain`.

## Spring and component scan

- `app-api` starts from package `com.dp9v.lingua_learn_server`, so it scans `com.dp9v.lingua_learn_server.*`.
- `app-bot` starts from package `com.dp9v.lingua_learn_server.bot`, so it scans `com.dp9v.lingua_learn_server.bot.*`, including the current `app-domain` bot DB package `com.dp9v.lingua_learn_server.bot.db`.
- `app-domain` should stay pluggable: its `@Repository`, `@Service`, and `@Component` classes should be picked up automatically when the module is added as a dependency and package scanning is compatible.
- `app-bot` manually creates only the Exposed `Database` bean through `Database.connect(dataSource)` in `DatabaseConfig`.
- Do not manually register repository beans from `app-domain` if they are already annotated with Spring stereotypes.
- If `app-api` uses `app-domain`, ensure `app-api` has `implementation(project(":app-domain"))` or equivalent and that package names remain under the scanned root package.

## Testing

### Backend

Run all project tests:

```powershell
cd A:\Code\lingua-learn-server
.\gradlew.bat test
```

Run bot-flow tests:

```powershell
cd A:\Code\lingua-learn-server
.\gradlew.bat :app-bot:test
```

Run API tests:

```powershell
cd A:\Code\lingua-learn-server
.\gradlew.bat :app-api:test
```

### UI

Run frontend tests and build:

```powershell
cd A:\Code\lingua-learn-server\app-ui\src
npm test
npm run build
```

Build UI through Gradle and copy it into API static resources:

```powershell
cd A:\Code\lingua-learn-server
.\gradlew.bat :app-ui:copyFrontend
```

### Local database

The root contains `compose.yaml` with PostgreSQL 17:

```powershell
cd A:\Code\lingua-learn-server
docker compose up -d postgres
```

Default parameters:

- DB: `lingua-learn-db`
- user: `user`
- password: `pass`
- port: `5432`

## Common change scenarios

### Add a new bot user field

1. Add a new Flyway migration in `app-api/src/main/resources/db/migration`.
2. Update the `BotUsers` table mapping in `app-domain`.
3. Update `BotUserDto`.
4. Update repository insert/update/select mapping in `app-domain`.
5. Update DTO creation sites in `app-bot`.
6. Add/update tests.

### Add a new bot state

1. Define the state in a bot model/enum if such enum is used.
2. Store state through a repository/service from `app-domain`.
3. Do not use in-memory storage.
4. Callback buttons must not automatically change state.
5. Cover the scenario with handler-flow tests.

### Add a new callback action

1. Add the action model in `app-bot/.../callback`.
2. When creating the button, save payload in `bot__callback_actions` through `BotCallbackActionRepository` from `app-domain`.
3. Put only `BotCallbackData.pack(id)` into `callback_data`.
4. In the callback handler, get the id through `BotCallbackData.unpack(data)`.
5. Process the payload and mark the action as consumed.
6. Do not change `bot__user_states` unless the task explicitly requires it.
7. Add tests for successful callback, repeated click, and expired/invalid callback.

### Change REST API for words/groups

1. Change `app-api` controller/service/DTO code.
2. Add DB query/repository/service logic in `app-domain` for new database access.
3. Add a migration in `app-api` if the schema changes.
4. Update OpenAPI.
5. Update `app-ui/src/src/api` and UI components.
6. Regenerate `openapi-java-client` if needed.
7. Run backend and frontend tests related to the change.

### Add new API data access

1. Keep HTTP/controller/service orchestration in `app-api`.
2. Add new DB query methods in `app-domain`.
3. Add `app-api -> app-domain` Gradle dependency if missing.
4. Ensure Spring can discover required domain beans.
5. Add tests at the appropriate level.

## What not to do

- Do not use `api/` as an active module unless explicitly requested.
- Do not add new DB query/data-access code directly to `app-api` or `app-bot`; add it to `app-domain`.
- Do not put bot DB access logic in `app-bot`.
- Do not put Telegram dependencies in `app-domain`.
- Do not manually edit generated OpenAPI client code.
- Do not merge `api__users` and `bot__users` into one model.
- Do not store full callback payloads in Telegram `callback_data`.
- Do not use a PostgreSQL `JSON` column through Exposed as a plain `String` unless a correct JSON column type is configured.

