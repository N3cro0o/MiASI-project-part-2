# 🏙️ Krasmap — Wrocławskie Krasnale

> **Drugi projekt wykonany w ramach zajęć MiASI (Modelowanie i analiza systemów informatycznych)**
>
> Interaktywna mapa POI (Point of Interest) Wrocławia — krasnali, zabytków, budynków, parków i miejsc — utrzymywana przez społeczność użytkowników.

---

## 📋 Spis treści

- [Opis projektu](#-opis-projektu)
- [Architektura](#-architektura)
- [Stos technologiczny](#-stos-technologiczny)
- [Struktura repozytorium](#-struktura-repozytorium)
- [Uruchomienie](#-uruchomienie)
- [Konteksty domenowe](#-konteksty-domenowe)
- [Role użytkowników](#-role-użytkowników)
- [Dokumentacja](#-dokumentacja)
- [Licencja](#-licencja)

---

## 📖 Opis projektu

**Krasmap** to system mapowania obiektów POI we Wrocławiu, inspirowany kultowymi wrocławskimi krasnalami. Aplikacja umożliwia:

- 🗺️ **Przeglądanie mapy** — interaktywna mapa z oznaczeniami krasnali i innych POI
- 🔍 **Filtrowanie** — wyszukiwanie obiektów wg kategorii (krasnal, zabytek, budynek, flora, miejsce)
- ⭐ **Recenzje** — komentarze i oceny (1–5) wystawiane przez użytkowników
- ✅ **Zgłoszenia** — proponowanie nowych krasnali z weryfikacją przez edytorów
- 📋 **Lista odwiedzonych** — prywatna lista obiektów oznaczonych jako odwiedzone
- 👥 **Zarządzanie użytkownikami** — rejestracja, logowanie, role i uprawnienia

---

## 🏗️ Architektura

Projekt oparty o zasady **Domain-Driven Design (DDD)** z architekturą **heksagonalną** i elementami **SOA/EDA**:

- **4 Bounded Contexts** — izolowane konteksty domenowe z miękkimi kluczami obcymi (soft FK)
- **Feature-Sliced Design (FSD)** — architektura frontendu z podziałem na feature'y
- **Event-Driven** — akceptacja zgłoszenia emituje event tworzący nowego krasnala

```
┌──────────────┐  REST API   ┌──────────────────┐   SQL    ┌────────────┐
│   Frontend   │ ──────────► │     Backend      │ ───────► │ PostgreSQL │
│  React + TS  │             │  Spring Boot 4   │          │    16      │
└──────────────┘             └──────────────────┘          └────────────┘
```

---

## 🛠️ Stos technologiczny

| Warstwa       | Technologie                                                     |
|---------------|-----------------------------------------------------------------|
| **Frontend**  | React 19, TypeScript, Vite, React Router, TanStack Query, Leaflet |
| **UI**        | React Bootstrap, Bootstrap 5                                    |
| **Backend**   | Java 17, Spring Boot 4, Spring Security, Spring Validation       |
| **API Docs**  | SpringDoc OpenAPI (Swagger UI)                                   |
| **Baza danych** | PostgreSQL 16 (Docker)                                        |
| **Konteneryzacja** | Docker Compose                                              |

---

## 📁 Struktura repozytorium

```
MiASI-project-part-2/
├── plans/                          # Dokumentacja projektowa
│   ├── 01-strategic.md             #   Analiza strategiczna DDD
│   ├── 02-tactical.md              #   Projekt taktyczny DDD
│   ├── 03-testing.md               #   Plan testowania
│   ├── 04-deployment.md            #   Plan wdrożenia
│   ├── database-design.md          #   Projekt bazy danych
│   └── diagrams/                   #   Diagramy (C4, sekwencji itp.)
│
├── project/
│   ├── schema.sql                  # Schemat bazy danych PostgreSQL
│   │
│   ├── database/
│   │   ├── docker-compose.yml      #   Docker Compose (PostgreSQL 16)
│   │   └── db-init/                #   Skrypty inicjalizujące bazę
│   │
│   ├── backend/                    # Spring Boot 4 (Java 17, Maven)
│   │   ├── pom.xml
│   │   └── src/
│   │       └── main/java/pl/...    #   Pakiety wg bounded contexts
│   │
│   └── frontend/                   # React 19 + TypeScript (Vite)
│       ├── package.json
│       └── src/
│           ├── app/                #   Konfiguracja aplikacji (router, providers)
│           ├── features/           #   Moduły domenowe (FSD)
│           │   ├── iam/            #     Logowanie, rejestracja, role
│           │   ├── poi-catalog/    #     Mapa, katalog krasnali
│           │   ├── verification/   #     Zgłoszenia i weryfikacja
│           │   └── interaction/    #     Recenzje, lista odwiedzonych
│           └── shared/             #   Współdzielone komponenty i utile
│
├── LICENSE                         # MIT License
└── README.md
```

---

## 🚀 Uruchomienie

### Wymagania

- **Node.js** ≥ 18 i **npm**
- **Java** 17 i **Maven**
- **Docker** i **Docker Compose**

### 1. Baza danych (PostgreSQL)

```bash
cd project/database
cp .env.example .env          # uzupełnij dane dostępowe
docker compose up -d
```

Schemat bazy (`schema.sql`) jest automatycznie ładowany przy pierwszym uruchomieniu kontenera przez `db-init/`.

### 2. Backend (Spring Boot)

```bash
cd project/backend
cp .env.example .env          # uzupełnij zmienne środowiskowe
./mvnw spring-boot:run
```

API będzie dostępne pod `http://localhost:8080`.  
Swagger UI: `http://localhost:8080/swagger-ui.html`

### 3. Frontend (React + Vite)

```bash
cd project/frontend
npm install
npm run dev
```

Aplikacja uruchomi się pod `http://localhost:5173`.

---

## 🧩 Konteksty domenowe

| Kontekst | Schema DB | Opis |
|----------|-----------|------|
| **IAM** | `iam` | Rejestracja, logowanie, zarządzanie rolami i kontami |
| **POI Catalog** | `poi_catalog` | Krasnale — nazwa, opis, współrzędne, kategoria, status |
| **Verification** | `verification` | Kolejka zgłoszeń nowych krasnali, workflow akceptacji/odrzucenia |
| **Interaction** | `interaction` | Recenzje (ocena + komentarz), lista odwiedzonych krasnali |

Konteksty komunikują się przez **miękkie klucze obce** (BIGINT bez deklaratywnych FK) — zapewnia to izolację bounded contexts.

---

## 👥 Role użytkowników

| Rola | Uprawnienia |
|------|-------------|
| **Gość** (`GUEST`) | Przeglądanie mapy, filtrowanie, podgląd recenzji |
| **Wędrowiec** (`WANDERER`) | + recenzje, lista odwiedzonych, zgłaszanie nowych krasnali |
| **Edytor** (`EDITOR`) | + edycja krasnali, weryfikacja zgłoszeń |
| **Admin** (`ADMIN`) | + zarządzanie kontami i rolami, dezaktywacja użytkowników |

---

## 📚 Dokumentacja

Szczegółowa dokumentacja projektowa znajduje się w katalogu `plans/`:

| Dokument | Zawartość |
|----------|-----------|
| [01-strategic.md](plans/01-strategic.md) | Dziedziny, konteksty, język wszechobecny, user stories, reguły biznesowe |
| [02-tactical.md](plans/02-tactical.md) | Agregaty, encje, value objects, repozytoria, serwisy domenowe |
| [03-testing.md](plans/03-testing.md) | Strategia testowania (unit, integracyjne, E2E) |
| [04-deployment.md](plans/04-deployment.md) | Plan wdrożenia i konfiguracja środowisk |
| [database-design.md](plans/database-design.md) | Projekt bazy danych, indeksy, widoki |

---

## 📝 Licencja

Projekt udostępniony na licencji [MIT](LICENSE).
