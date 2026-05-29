# 04-deployment.md — Integracja i Wdrożenie
> Krasmap — System mapowania POI utrzymywany przez społeczność
> Odpowiedzialność: Kierownik Implementacji

---

## 1. Wymagania Środowiskowe

| Narzędzie | Wersja minimalna | Weryfikacja |
|---|---|---|
| Java (JDK) | 17 | `java -version` |
| Maven | 3.9+ | `mvn -version` |
| Docker | 24+ | `docker -version` |
| Docker Compose | 2.x (plugin) | `docker compose version` |
| Git | dowolna | `git --version` |

> **Uwaga:** Docker jest jedynym wymaganiem dla bazy danych.
> PostgreSQL **nie musi** być zainstalowany lokalnie.

---

## 2. Szybki Start (3 komendy)

```bash
# 1. Sklonuj repozytorium
git clone https://github.com/your-org/krasmap.git && cd krasmap

# 2. Uruchom bazę danych
docker compose up -d

# 3. Uruchom aplikację
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

Aplikacja dostępna pod: **http://localhost:8080**

---

## 3. Struktura Projektu

```
krasmap/
├── plans/                        ← dokumentacja (DDD, testy, wdrożenie)
├── db/
│   └── schema.sql                ← referencyjny schemat bazy danych
├── src/
│   ├── main/
│   │   ├── java/pl/krasmap/
│   │   │   ├── KrasmapApplication.java
│   │   │   ├── iam/
│   │   │   ├── poicatalog/
│   │   │   ├── verification/
│   │   │   └── interaction/
│   │   └── resources/
│   │       ├── application.yml          ← główna konfiguracja
│   │       ├── application-dev.yml      ← profil deweloperski
│   │       ├── application-prod.yml     ← profil produkcyjny
│   │       ├── db/migration/            ← skrypty Flyway
│   │       │   ├── V1__create_schemas_and_enums.sql
│   │       │   ├── V2__create_iam_tables.sql
│   │       │   ├── V3__create_poi_catalog_tables.sql
│   │       │   ├── V4__create_verification_tables.sql
│   │       │   ├── V5__create_interaction_tables.sql
│   │       │   └── V6__seed_data.sql
│   │       ├── static/
│   │       │   ├── css/
│   │       │   └── js/
│   │       └── templates/               ← szablony Thymeleaf
│   └── test/
│       └── java/pl/krasmap/
├── .env.example                  ← szablon zmiennych środowiskowych
├── .env                          ← lokalne zmienne (NIE commitować!)
├── docker-compose.yml
├── docker-compose.prod.yml
├── Dockerfile
├── pom.xml
└── README.md
```

---

## 4. Zmienne Środowiskowe

### `.env.example` — skopiuj do `.env` i uzupełnij

```dotenv
# ── PostgreSQL ────────────────────────────────────────────────────
POSTGRES_HOST=localhost
POSTGRES_PORT=5432
POSTGRES_DB=krasmap
POSTGRES_USER=krasmap_user
POSTGRES_PASSWORD=change_me_in_production

# ── Spring Boot ───────────────────────────────────────────────────
SPRING_PROFILES_ACTIVE=dev
SERVER_PORT=8080

# ── Spring Security ───────────────────────────────────────────────
# Sekret do podpisywania sesji / tokenów
APP_SECURITY_SECRET=change_me_min_32_characters_long!!

# ── Leaflet.js / Mapa ─────────────────────────────────────────────
# Domyślne centrum mapy (Wrocław — Rynek)
MAP_DEFAULT_LAT=51.1099
MAP_DEFAULT_LNG=17.0318
MAP_DEFAULT_ZOOM=14
```

> ⚠️ Plik `.env` jest w `.gitignore`. Nigdy nie commituj haseł do repozytorium.

---

## 5. Docker Compose

### `docker-compose.yml` — środowisko deweloperskie

```yaml
services:

  postgres:
    image: postgres:16-alpine
    container_name: krasmap-postgres
    restart: unless-stopped
    env_file: .env
    environment:
      POSTGRES_DB:       ${POSTGRES_DB}
      POSTGRES_USER:     ${POSTGRES_USER}
      POSTGRES_PASSWORD: ${POSTGRES_PASSWORD}
    ports:
      - "${POSTGRES_PORT:-5432}:5432"
    volumes:
      - krasmap_pgdata:/var/lib/postgresql/data
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U ${POSTGRES_USER} -d ${POSTGRES_DB}"]
      interval: 10s
      timeout: 5s
      retries: 5

  # ── Opcjonalne: pgAdmin do podglądu bazy ────────────────────────
  pgadmin:
    image: dpage/pgadmin4:latest
    container_name: krasmap-pgadmin
    restart: unless-stopped
    environment:
      PGADMIN_DEFAULT_EMAIL:    admin@krasmap.local
      PGADMIN_DEFAULT_PASSWORD: admin
    ports:
      - "5050:80"
    depends_on:
      postgres:
        condition: service_healthy
    profiles:
      - tools  # uruchom tylko z: docker compose --profile tools up

volumes:
  krasmap_pgdata:
```

### Komendy Docker Compose

```bash
# Uruchom tylko bazę danych (tło)
docker compose up -d postgres

# Uruchom z pgAdmin
docker compose --profile tools up -d

# Sprawdź status kontenerów
docker compose ps

# Logi PostgreSQL na żywo
docker compose logs -f postgres

# Zatrzymaj kontenery (dane zachowane)
docker compose stop

# Usuń kontenery I dane (reset bazy)
docker compose down -v
```

---

## 6. Konfiguracja Spring Boot

### `src/main/resources/application.yml`

```yaml
spring:
  application:
    name: krasmap

  # Flyway — migracje bazy danych
  flyway:
    enabled: true
    locations: classpath:db/migration
    baseline-on-migrate: true
    schemas: iam, poi_catalog, verification, interaction

  # JPA — wspólne ustawienia
  jpa:
    open-in-view: false
    properties:
      hibernate:
        format_sql: true

  # Thymeleaf
  thymeleaf:
    cache: false   # wyłącz w dev, włącz w prod

server:
  port: ${SERVER_PORT:8080}
  error:
    include-message: always

# Konfiguracja mapy (Leaflet.js)
app:
  map:
    default-lat:  ${MAP_DEFAULT_LAT:51.1099}
    default-lng:  ${MAP_DEFAULT_LNG:17.0318}
    default-zoom: ${MAP_DEFAULT_ZOOM:14}
  security:
    secret: ${APP_SECURITY_SECRET:dev-secret-not-for-production}
```

### `src/main/resources/application-dev.yml`

```yaml
spring:
  datasource:
    url:      jdbc:postgresql://${POSTGRES_HOST:localhost}:${POSTGRES_PORT:5432}/${POSTGRES_DB:krasmap}
    username: ${POSTGRES_USER:krasmap_user}
    password: ${POSTGRES_PASSWORD:dev_password}
    driver-class-name: org.postgresql.Driver

  jpa:
    hibernate:
      ddl-auto: validate   # Flyway zarządza schematem, Hibernate tylko waliduje
    show-sql: true

logging:
  level:
    pl.krasmap: DEBUG
    org.springframework.web: INFO
    org.hibernate.SQL: DEBUG
```

### `src/main/resources/application-prod.yml`

```yaml
spring:
  datasource:
    url:      jdbc:postgresql://${POSTGRES_HOST}:${POSTGRES_PORT}/${POSTGRES_DB}
    username: ${POSTGRES_USER}
    password: ${POSTGRES_PASSWORD}
    hikari:
      maximum-pool-size: 10
      minimum-idle: 2
      connection-timeout: 30000

  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false

  thymeleaf:
    cache: true

logging:
  level:
    pl.krasmap: INFO
    root: WARN
```

---

## 7. Migracje Bazy Danych (Flyway)

Flyway uruchamia skrypty automatycznie przy starcie aplikacji w kolejności wersji.
Skrypty znajdują się w `src/main/resources/db/migration/`.

### Konwencja nazewnictwa

```
V{numer}__{opis}.sql

Przykłady:
  V1__create_schemas_and_enums.sql
  V2__create_iam_tables.sql
  V6__seed_data.sql
```

> **Zasada:** skryptu Flyway **nigdy nie modyfikujemy** po tym, jak był uruchomiony
> na jakimkolwiek środowisku. Każda zmiana = nowy plik z wyższym numerem wersji.

### Podział pliku `db/schema.sql` na migracje Flyway

| Plik migracji | Zawartość |
|---|---|
| `V1__create_schemas_and_enums.sql` | `CREATE SCHEMA`, `CREATE TYPE` (enumy), funkcja `update_updated_at_column()` |
| `V2__create_iam_tables.sql` | `iam.system_users` + indeksy |
| `V3__create_poi_catalog_tables.sql` | `poi_catalog.dwarfs` + trigger + indeksy + widoki |
| `V4__create_verification_tables.sql` | `verification.submissions` + indeksy + widoki |
| `V5__create_interaction_tables.sql` | `interaction.reviews` + `interaction.visited_entries` + indeksy + widoki |
| `V6__seed_data.sql` | Konto admin, konto editor, przykładowe Krasnale |

### Ręczne uruchomienie migracji (bez startu aplikacji)

```bash
./mvnw flyway:migrate \
  -Dflyway.url=jdbc:postgresql://localhost:5432/krasmap \
  -Dflyway.user=krasmap_user \
  -Dflyway.password=dev_password
```

### Sprawdzenie stanu migracji

```bash
./mvnw flyway:info \
  -Dflyway.url=jdbc:postgresql://localhost:5432/krasmap \
  -Dflyway.user=krasmap_user \
  -Dflyway.password=dev_password
```

---

## 8. Budowanie i Uruchamianie

### Lokalne uruchomienie (profil dev)

```bash
# Krok 1 — uruchom bazę danych
docker compose up -d postgres

# Krok 2 — poczekaj na gotowość bazy (healthcheck)
docker compose ps   # STATUS powinien być "healthy"

# Krok 3 — uruchom aplikację
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# Alternatywnie — z plikiem .env
export $(cat .env | xargs) && ./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### Budowanie JAR (produkcja)

```bash
# Pomiń testy (szybkie budowanie)
./mvnw clean package -DskipTests

# Z testami (zalecane przed deployem)
./mvnw clean verify

# Uruchom JAR
java -jar target/krasmap-1.0.0.jar --spring.profiles.active=prod
```

---

## 9. Dockerfile

```dockerfile
# ── Etap 1: budowanie ────────────────────────────────────────────
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app

COPY mvnw pom.xml ./
COPY .mvn .mvn
RUN ./mvnw dependency:go-offline -q

COPY src ./src
RUN ./mvnw clean package -DskipTests -q

# ── Etap 2: obraz produkcyjny (tylko JRE) ────────────────────────
FROM eclipse-temurin:17-jre-alpine AS runtime
WORKDIR /app

RUN addgroup -S krasmap && adduser -S krasmap -G krasmap

COPY --from=builder /app/target/krasmap-*.jar app.jar

USER krasmap
EXPOSE 8080

ENTRYPOINT ["java", "-jar", \
            "-Dspring.profiles.active=prod", \
            "-XX:+UseContainerSupport", \
            "-XX:MaxRAMPercentage=75.0", \
            "app.jar"]
```

### `docker-compose.prod.yml` — pełny stack produkcyjny

```yaml
services:

  postgres:
    image: postgres:16-alpine
    container_name: krasmap-postgres
    restart: always
    environment:
      POSTGRES_DB:       ${POSTGRES_DB}
      POSTGRES_USER:     ${POSTGRES_USER}
      POSTGRES_PASSWORD: ${POSTGRES_PASSWORD}
    volumes:
      - krasmap_pgdata:/var/lib/postgresql/data
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U ${POSTGRES_USER} -d ${POSTGRES_DB}"]
      interval: 10s
      timeout: 5s
      retries: 5
    networks:
      - krasmap-net

  app:
    build: .
    container_name: krasmap-app
    restart: always
    env_file: .env
    environment:
      SPRING_PROFILES_ACTIVE: prod
      POSTGRES_HOST:          postgres
    ports:
      - "8080:8080"
    depends_on:
      postgres:
        condition: service_healthy
    networks:
      - krasmap-net
    healthcheck:
      test: ["CMD-SHELL", "wget -qO- http://localhost:8080/actuator/health || exit 1"]
      interval: 30s
      timeout: 10s
      retries: 3

networks:
  krasmap-net:
    driver: bridge

volumes:
  krasmap_pgdata:
```

### Uruchomienie produkcyjne

```bash
# Zbuduj i uruchom pełny stack
docker compose -f docker-compose.prod.yml --env-file .env up -d --build

# Sprawdź logi aplikacji
docker compose -f docker-compose.prod.yml logs -f app

# Health check
curl http://localhost:8080/actuator/health
```

---

## 10. Konfiguracja Spring Security

### Mapowanie ról na URL (w warstwie infrastruktury)

```java
// src/main/java/pl/krasmap/iam/infrastructure/SecurityConfig.java

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // GUEST — publiczny dostęp
                .requestMatchers("/", "/map", "/api/dwarfs/**").permitAll()
                .requestMatchers("/auth/login", "/auth/register").permitAll()
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/actuator/health").permitAll()

                // USER — zalogowany
                .requestMatchers(HttpMethod.POST, "/dwarfs/*/reviews").hasRole("USER")
                .requestMatchers(HttpMethod.POST, "/submissions").hasRole("USER")
                .requestMatchers("/visited/**").hasRole("USER")

                // EDITOR — weryfikacja
                .requestMatchers("/submissions/*/accept").hasRole("EDITOR")
                .requestMatchers("/submissions/*/reject").hasRole("EDITOR")
                .requestMatchers("/submissions/pending").hasRole("EDITOR")
                .requestMatchers(HttpMethod.PUT, "/dwarfs/**").hasRole("EDITOR")

                // ADMIN — zarządzanie
                .requestMatchers("/admin/**").hasRole("ADMIN")

                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/auth/login")
                .defaultSuccessUrl("/map", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/auth/logout")
                .logoutSuccessUrl("/map")
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
```

---

## 11. Uruchamianie Testów

```bash
# Wszystkie testy (wymaga działającego Dockera dla Testcontainers)
./mvnw verify

# Tylko testy jednostkowe (bez Dockera)
./mvnw test -Dgroups="unit"

# Tylko testy integracyjne
./mvnw verify -Dgroups="integration"

# Tylko testy architektoniczne
./mvnw test -Dtest="*ArchitectureTest"

# Raport pokrycia kodu (JaCoCo)
./mvnw verify
# Raport HTML w: target/site/jacoco/index.html
open target/site/jacoco/index.html
```

### Adnotacje do grupowania testów

```java
// Oznaczanie testów jednostkowych
@Tag("unit")
class RatingTest { ... }

// Oznaczanie testów integracyjnych
@Tag("integration")
class JpaDwarfRepositoryTest extends BaseIntegrationTest { ... }
```

---

## 12. Weryfikacja Po Uruchomieniu

Po starcie aplikacji sprawdź:

```bash
# 1. Health check (Spring Actuator)
curl http://localhost:8080/actuator/health
# Oczekiwana odpowiedź: {"status":"UP"}

# 2. Strona główna
curl -I http://localhost:8080/map
# Oczekiwana odpowiedź: HTTP/1.1 200

# 3. API — lista aktywnych Krasnali
curl http://localhost:8080/api/dwarfs?status=ACTIVE
# Oczekiwana odpowiedź: JSON z przykładowymi Krasnalami (seed data)

# 4. Sprawdź schematy w bazie danych
docker exec -it krasmap-postgres \
  psql -U krasmap_user -d krasmap \
  -c "\dn"
# Oczekiwana odpowiedź: iam, poi_catalog, verification, interaction
```

---

## 13. Rozwiązywanie Problemów

| Problem | Możliwa przyczyna | Rozwiązanie |
|---|---|---|
| `Connection refused` na port 5432 | Kontener nie działa | `docker compose up -d postgres` |
| `Flyway migration failed` | Plik migracji zmieniony po uruchomieniu | Nie modyfikuj istniejących plików V*.sql |
| `Table not found` | Flyway nie uruchomił migracji | Sprawdź `spring.flyway.enabled=true` |
| `ApplicationContext fails to load` | Brak zmiennych środowiskowych | Skopiuj `.env.example` do `.env` i uzupełnij |
| Testy integracyjne nie startują | Docker nie działa | Uruchom Docker Desktop |
| `Port 8080 already in use` | Inna aplikacja na porcie | `SERVER_PORT=8081` w `.env` |
| `ArchUnit test failed` | Naruszenie hexagonal architecture | Sprawdź importy — brak Springa w `domain` |

---

## 14. Zależności Maven (`pom.xml` — kluczowe fragmenty)

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.3.0</version>
</parent>

<properties>
    <java.version>17</java.version>
    <testcontainers.version>1.19.8</testcontainers.version>
    <archunit.version>1.3.0</archunit.version>
</properties>

<dependencies>
    <!-- Web + Thymeleaf -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-thymeleaf</artifactId>
    </dependency>
    <dependency>
        <groupId>org.thymeleaf.extras</groupId>
        <artifactId>thymeleaf-extras-springsecurity6</artifactId>
    </dependency>

    <!-- Security -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>

    <!-- JPA + PostgreSQL -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <scope>runtime</scope>
    </dependency>

    <!-- Flyway — migracje -->
    <dependency>
        <groupId>org.flywaydb</groupId>
        <artifactId>flyway-core</artifactId>
    </dependency>
    <dependency>
        <groupId>org.flywaydb</groupId>
        <artifactId>flyway-database-postgresql</artifactId>
    </dependency>

    <!-- Walidacja -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>

    <!-- Actuator — health check -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>

    <!-- Testy -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework.security</groupId>
        <artifactId>spring-security-test</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.testcontainers</groupId>
        <artifactId>junit-jupiter</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.testcontainers</groupId>
        <artifactId>postgresql</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>com.tngtech.archunit</groupId>
        <artifactId>archunit-junit5</artifactId>
        <version>${archunit.version}</version>
        <scope>test</scope>
    </dependency>
</dependencies>

<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.testcontainers</groupId>
            <artifactId>testcontainers-bom</artifactId>
            <version>${testcontainers.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```