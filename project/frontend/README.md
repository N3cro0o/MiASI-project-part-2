# Wrocławskie Krasnale - Frontend UI (React + Vite + TypeScript)

To jest aplikacja frontendowa dla Wrocławskie Krasnale, zbudowana przy użyciu bibliotek React, TypeScript, Vite i Leaflet.

## 🚀 Szybki start

Upewnij się, że masz zainstalowanego środowisko [Node.js](https://nodejs.org/) (zalecana wersja v20 lub nowsza).
**Ważne:** Używamy wyłącznie `npm` jako naszego menedżera pakietów. Nie używaj `yarn` ani `pnpm`, aby uniknąć konfliktów w plikach blokujących (lockfile).

### 1. Instalacja zależności
```bash
npm install
```

### 2. Konfiguracja środowiska
Utwórz plik `.env` w głównym katalogu frontendu.
```properties
# Adres URL do naszego backendu Spring Boot
VITE_API_URL=http://localhost:8080/api
```

### 3. Uruchomienie serwera deweloperskiego
```bash
npm run dev
```
Aplikacja będzie dostępna pod adresem http://localhost:5173.

---

## 📂 Struktura Architektoniczna (Feature-Sliced)

Aby dostosować się do granic DDD wyznaczonych na backendzie, kod frontendu jest podzielony na moduły funkcjonalne (features):

* `src/features/iam/` - Zarządzanie Tożsamością i Dostępem (Logowanie, Role).
* `src/features/poi-catalog/` - Główna mapa, wizualizacja POI oraz filtrowanie.
* `src/features/verification/` - Formularze zgłoszeń oraz kolejka akceptacji dla Edytora.
* `src/features/interaction/` - Recenzje, oceny oraz prywatna lista odwiedzonych obiektów.
* `src/shared/` - Komponenty UI wielokrotnego użytku (przyciski, okna modalne) oraz globalni klienci API.

## 🗺️ Biblioteka Mapy
Używamy `react-leaflet` do renderowania interaktywnej mapy wewnątrz modułu `poi-catalog`. Upewnij się, że wszystkie nowe komponenty mapy są poprawnie zagnieżdżone wewnątrz tagu `<MapContainer>`.
