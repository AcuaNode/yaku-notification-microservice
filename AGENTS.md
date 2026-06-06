# AGENTS.md — notification-service

## Project basics
- Spring Boot 3.4.3 / Java 17 / Maven single-module project.
- Package root: `notification_service` (underscore). The root `pom.xml` declares `groupId=io.github.rafaviv` but source code is not under that package path.
- No `README`, no CI workflows. A `compose.yaml` exists for local Postgres + Kafka.

## Developer commands
Use the Maven wrapper (Maven 3.9.16):
```bash
./mvnw compile
./mvnw test
./mvnw spring-boot:run
./mvnw package
./mvnw -Dtest=NotificationServiceApplicationTests test
```

## Dev environment defaults
- Active profile is `dev` (`spring.profiles.active=dev` in `application.properties`).
- Dev DB: Postgres at `localhost:5435/yaku_notifications` (user `root` / `password`).
- Dev Kafka: `localhost:9093`.
- App runs on port **8083** (`server.port=8083`).
- `spring-boot-docker-compose` dependency is commented out in `pom.xml`; run `docker compose up -d` manually for local infra.

## Architecture notes
- DDD-lite folder structure: `domain` / `application/internal` / `infrastructure` / `interfaces/rest`.
- `Notification` and `DeviceToken` are domain aggregates (plain Java objects). JPA entities are `NotificationEntity` and `DeviceTokenEntity` in `infrastructure/persistance/jpa`.
- FCM push notifications are mocked: `FcmClient` implements `PushNotificationService` and only prints to stdout.
- OpenAPI/Swagger is auto-configured via `springdoc-openapi-starter-webmvc-ui` 2.8.8; config bean reads from `documentation.application.*` properties populated by Maven resource filtering.

## Repo-specific conventions
- Lombok is used (`@Getter`, `@Setter`) and must be registered as an annotation processor (already configured in `pom.xml`).
- Resource classes use Java records for REST request/response payloads where possible.
- REST base path: `/api/v1`.

## Known issues (see `BUGS.md`)
- Tests require a running database; no `application-test.properties` overrides datasource to H2.
- `DeviceTokenEntity` has no unique constraint on `(userId, fcmToken)`.
- Application services are not annotated with `@Transactional`.
- `NotificationsController` directly injects `NotificationJpaRepository` instead of using an application command service.
