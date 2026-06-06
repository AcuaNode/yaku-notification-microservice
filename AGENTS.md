# AGENTS.md — notification-service

## Project basics
- Spring Boot 4.0.6 / Java 17 / Maven single-module project.
- Active source lives at the **repo root**. A stale nested `notification-service/` directory exists — ignore it completely.
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

## Known startup / test blockers (see `BUGS.md`)
1. **Spring context fails on load** because `application.properties` references a class that does not exist in this repo:
   `spring.jpa.hibernate.naming.physical-strategy=io.github.rafaviv.yakubackend.shared.infrastructure.persistence.jpa.configuration.strategy.SnakeCaseWithPluralizedTablePhysicalNamingStrategy`
   Fix: remove that property or copy the class into this repo.
2. **Stale template artifact**: `spring.application.name=equipment-service` in `application.properties`.
3. **Tests need a running database** — H2 is on the test classpath but there is no `application-test.properties` or datasource override, so the single `@SpringBootTest` (`NotificationServiceApplicationTests`) will fail without a reachable Postgres instance.

## Dev environment defaults
- Active profile is `dev` (`spring.profiles.active=dev` in `application.properties`).
- Dev DB: Postgres at `localhost:5432/yaku_equipment` (user `root` / `password`).
- Dev Kafka: `localhost:9092`.
- App runs on port **8083** (`server.port=8083`).
- `spring-boot-docker-compose` dependency is commented out in `pom.xml`; run `docker compose up -d` manually for local infra.

## Architecture notes
- DDD-lite folder structure: `domain` / `application/internal` / `infrastructure` / `interfaces/rest`.
- `Notification` and `DeviceToken` are domain aggregates (plain Java objects). JPA entities are `NotificationEntity` and `DeviceTokenEntity` in `infrastructure/persistance/jpa`.
- FCM push notifications are mocked: `FcmClient` implements `PushNotificationService` and only prints to stdout.
- Kafka event publisher (`KafkaDomainEventPublisher`) uses a manually configured `KafkaTemplate<String, String>`.
- OpenAPI/Swagger is auto-configured via `springdoc-openapi-starter-webmvc-ui` 2.8.8; config bean reads from `documentation.application.*` properties populated by Maven resource filtering.

## Repo-specific conventions
- Lombok is used (`@Getter`, `@Setter`) and must be registered as an annotation processor (already configured in `pom.xml`).
- Resource classes use Java records for REST request/response payloads where possible.
- REST base path: `/api/v1`.
