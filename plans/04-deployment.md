IMO do zredagowania, większość patentów z stąd jest useless


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
│   │   ├── pl/krasmap/
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
│       └── pl/krasmap/
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

## 11. Uruchamianie Testów
