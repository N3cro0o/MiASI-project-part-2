# Krasmap
> System mapowania punktów użyteczności publicznej - POI (ang. Point of interest) utrzymywany przez społeczność

---

## 1. Dziedziny
### Główna

* **Mapowanie położenia obiektów**  
Głównym aspektem projektu jest przechowywanie podstawowych informacji o *Krasnalach* i obrazowanie ich pozycji na mapie. W tym celu przechowywane są dane geolokalizacyjne każdego z obiektów. *Krasnale* mają przypisane kategorie, wedle których można filtrować ich obecność na mapie. Użytkownik ma możliwość dodawania *Krasnali* do indywidualnych kategorii, m.in. listy odwiedzonych *Krasnali*.

### Wspierające

* **Weryfikacja nowych zgłoszeń dodania obiektów**  
Dodatkowy aspekt projektu pozwalający użytkownikom na dodawanie nowych *Krasnali*. Po poprawnego dodania obiektu do aplikacji niezbędna jest weryfikacja administratora.

* **Dodawanie komentarzy i ocen do obiektów**  
Dodatkowy aspekt projektu rozszerzający dostępne dane o *Krasnalach*. Użytkownicy aplikacji mają możliwość komentować i oceniać już odwiedzone obiekty w systemie.

### Ogólna

* **Zarządzanie tożsamością i dostępem do danych (IAM - ang. Identity and Access Management)**  
Ogólny aspekt projektu pzowajalający na rejestrację, logowanie i zarządzanie Rolami użytkowników systemu.

---

## 2. Konteksty

### Kontekst Krasnal
Przechowywanie i manipulowanie informacjami dotyczącymi *Krasnali*: współrzędnymi, nazwą, opisem, kategorią i statusem. 

### Kontekst Zgłoszenie
Kolejka propozycji nowych *Krasnali* przesyłanych przez użytkowników, oczekujących
na akceptację lub odrzucenie przez Edytora bądź Admina.

### Kontekst Interakcja
Komentarze i oceny wystawiane przez użytkowników dla *Krasnali* oraz prywatna lista
obiektów oznaczonych przez użytkownika jako odwiedzone.

### Kontekst IAM (ang. Identity and Access Management)
Rejestracja, uwierzytelnianie (logowanie) oraz autoryzacja (zarządzanie Rolami
użytkowników systemu). Stanowi jedyny kontekst, który zna tożsamość użytkownika.

---

## 3. Język Wszechobecny
 
### Kontekst Krasnali — *POI Catalog Context*
 
| Termin PL | Termin EN | Definicja |
|---|---|---|
| Krasnal | *Krasnal* | Obiekt świata rzeczywistego zobrazowany na mapie. Posiada nazwę, opis, współrzędne, kategorię i status. |
| Nazwa | *Name* | Krótka identyfikująca etykieta Krasnala (max 255 znaków). |
| Opis | *Description* | Tekstowa informacja o historii i charakterystyce Krasnala. |
| Lokalizacja | *location* | Położenie geograficzne Krasnala wyrażone parą współrzędnych (szerokość, długość). |
| Współrzędne | *Coordinates* | Wartość złożona: `latitude` i `longitude` z walidacją zakresu. |
| Kategoria | *Category* | Enum klasyfikujący Krasnala: `MONUMENT`, `BUILDING`, `DWARF_FIGURINE`, `FLORA`, `PLACE`. |
| Status | *Status* | Enum określający widoczność Krasnala: `ACTIVE`, `INACTIVE`, `ARCHIVED`. |
 
### Kontekst Zgłoszeń — *Verification Context*
 
| Termin PL | Termin EN | Definicja |
|---|---|---|
| Zgłoszenie | *Submission* | Propozycja dodania nowego Krasnala przesłana przez użytkownika. Zawiera wszystkie dane wymagane do stworzenia Krasnala. |
| Ładunek zgłoszenia | *Submission Payload* | Zestaw danych proponowanego Krasnala przechowywany jako JSONB do momentu weryfikacji. |
| Weryfikacja | *Verification* | Proces przeglądu Zgłoszenia przez Edytora lub Admina. Może zakończyć się akceptacją, edycją lub odrzuceniem. |
| Powód odrzucenia | *Rejection Reason* | Obowiązkowe wyjaśnienie podawane przez weryfikującego w przypadku odrzucenia Zgłoszenia. |
| Status zgłoszenia | *Submission Status* | Enum: `PENDING` (oczekuje), `ACCEPTED` (zaakceptowane), `REJECTED` (odrzucone). |
 
### Kontekst Interakcji — *Interaction Context*
 
| Termin PL | Termin EN | Definicja |
|---|---|---|
| Komentarz | *Comment* (część Review) | Tekstowa adnotacja użytkownika odnosząca się do konkretnego Krasnala (max 2000 znaków). |
| Ocena | *Rating* | Wartość całkowita od 1 do 5 wystawiana przez użytkownika dla Krasnala. |
| Recenzja | *Review* | Agregat łączący Ocenę i Komentarz — użytkownik wystawia je razem. |
| Średnia ocen | *Average Score* | Wartość wyliczana na żądanie ze wszystkich Recenzji danego Krasnala. |
| Lista odwiedzonych | *Visited List* | Prywatna lista Krasnali oznaczonych przez konkretnego użytkownika jako odwiedzone. |
| Wpis odwiedzenia | *Visited Entry* | Pojedynczy rekord na liście odwiedzonych: powiązanie użytkownika z Krasnale i czas oznaczenia. |
 
### Kontekst IAM — *IAM Context*
 
| Termin PL | Termin EN | Definicja |
|---|---|---|
| Użytkownik systemu | *System User* | (Nie mylić z Rolą USER) — każda osoba posiadająca konto w systemie. |
| Rola | *Role* | Enum określający poziom uprawnień w systemie: `GUEST`, `USER`, `EDITOR`, `ADMIN`. |
| Rejestracja | *Registration* | Proces założenia konta przez Gościa w celu uzyskania Roli `USER`. |
| Logowanie | *login* | Proces potwierdzenia tożsamości przez użytkownika systemu w celu uzyskania sesji. |
| Gość | *Guest* | Niezalogowana osoba. Rola `GUEST` — może przeglądać mapę i filtrować Krasnale. |
| Użytkownik | *User* | Zalogowana osoba z Rolą `USER` — może dodawać Recenzje, oznaczać Krasnale jako odwiedzone i zgłaszać nowe. |
| Edytor | *Editor* | Zalogowana osoba z Rolą `EDITOR` — może edytować Krasnale i weryfikować Zgłoszenia. |
| Admin | *Admin* | Najwyższy poziom uprawnień. Posiada wszystkie uprawnienia Edytora, plus zarządzanie kontami i Rolami. |

---

## 4. Historyjki użytkownika
 
### Gość
 
* **US1**: Jako gość chcę oglądać mapę *Krasnali*, aby wiedzieć, gdzie mogę znaleźć atrakcje we Wrocławiu.
  * **Kryteria akceptacyjne:**
    - na mapie wyświetlają się *Krasnale* o statusie `ACTIVE`,
    - *Krasnale* nie znajdują się poza obszarem Wrocławia.
* **US2**: Jako gość chcę filtrować *Krasnale* na mapie na podstawie kategorii, aby widzieć jedynie te obiekty, które mnie interesują.
  * **Kryteria akceptacyjne:**
    - wyświetlane na mapie *Krasnale* mają przypisaną jedną z wybranych przeze mnie kategorii.
* **US3**: Jako gość chcę wyświetlać informacje o *Krasnalach* (nazwa, opis, kategoria, średnia ocen, komentarze), aby zapoznać się z ich charakterystyką i opiniami użytkowników.
  * **Kryteria akceptacyjne:**
    - widoczna jest nazwa i opis *Krasnala*,
    - widoczne są Recenzje wraz z nazwą użytkownika,
    - widoczna jest średnia ocen wszystkich użytkowników.
* **US4**: Jako gość chcę zarejestrować się do systemu, aby uzyskać Rolę Użytkownika i móc korzystać z funkcji dostępnych tylko dla zalogowanych.
  * **Kryteria akceptacyjne:**
    - po rejestracji mogę się zalogować i uzyskuję dostęp do funkcji Użytkownika.
---
 
### Użytkownik
 
* **US5**: Jako użytkownik chcę logować się do systemu, aby uzyskać dostęp do swoich danych i uprawnień.
  * **Kryteria akceptacyjne:**
    - moja lista odwiedzonych *Krasnali* nie zmieniła się względem ostatniej sesji,
    - mogę dodawać Recenzje do *Krasnali*,
    - mogę wysłać Zgłoszenie nowego *Krasnala*.
* **US6**: Jako użytkownik chcę dodawać Recenzje (komentarz + ocena) do *Krasnali*, aby pozostali użytkownicy i goście znali moją opinię.
  * **Kryteria akceptacyjne:**
    - widzę swoją ocenę i komentarz w sekcji wystawionych przeze mnie Recenzji,
    - pozostali goście i użytkownicy widzą mój komentarz i ocenę.
* **US7**: Jako użytkownik chcę usuwać własne Recenzje, aby móc zarządzać swoją aktywnością w systemie.
  * **Kryteria akceptacyjne:**
    - usunięta Recenzja nie jest widoczna dla żadnego użytkownika ani gościa.
* **US8**: Jako użytkownik chcę dodawać *Krasnale* do swojej listy odwiedzonych, aby śledzić odwiedzone atrakcje.
  * **Kryteria akceptacyjne:**
    - w mojej liście odwiedzonych *Krasnali* znajduje się właśnie dodany *Krasnal*,
    - na mapie mogę filtrować odwiedzone *Krasnale*, aby wyświetlały się tylko te nieodwiedzone.
* **US9**: Jako użytkownik chcę usuwać *Krasnale* ze swojej listy odwiedzonych, aby korygować swoje wpisy.
  * **Kryteria akceptacyjne:**
    - usunięty *Krasnal* nie widnieje na mojej liście odwiedzonych.
* **US10**: Jako użytkownik chcę zgłaszać nowe *Krasnale*, aby mapa była aktualna i bogatsza w informacje.
  * **Kryteria akceptacyjne:**
    - Zgłoszenie jest widoczne w mojej sekcji Zgłoszeń ze statusem `PENDING`.
* **US11**: Jako użytkownik chcę przeglądać status moich Zgłoszeń, aby wiedzieć, czy zostały zaakceptowane lub odrzucone (wraz z powodem odrzucenia).
  * **Kryteria akceptacyjne:**
    - widzę aktualny status każdego ze swoich Zgłoszeń (`PENDING`, `ACCEPTED`, `REJECTED`),
    - dla statusu `REJECTED` widoczny jest powód odrzucenia podany przez weryfikującego.
---
 
### Edytor
 
* **US12**: Jako edytor chcę edytować dane *Krasnali* (nazwa, opis, kategoria, status), aby informacje były aktualne i poprawne.
  * **Kryteria akceptacyjne:**
    - w sekcji *Krasnala* widoczne są jego nowe dane po edycji.
* **US13**: Jako edytor chcę przeglądać listę oczekujących Zgłoszeń, aby podejmować decyzje weryfikacyjne.
  * **Kryteria akceptacyjne:**
    - widzę listę Zgłoszeń ze statusem `PENDING` wraz z ich ładunkiem danych.
* **US14**: Jako edytor chcę akceptować Zgłoszenia, aby nowe *Krasnale* pojawiały się na mapie.
  * **Kryteria akceptacyjne:**
    - po akceptacji nowy *Krasnal* pojawia się na mapie ze statusem `ACTIVE`,
    - Zgłoszenie zmienia status na `ACCEPTED`.
* **US15**: Jako edytor chcę odrzucać Zgłoszenia z podaniem powodu, aby zgłaszający wiedział, dlaczego jego propozycja nie przeszła weryfikacji.
  * **Kryteria akceptacyjne:**
    - Zgłoszenie zmienia status na `REJECTED`,
    - zgłaszający widzi powód odrzucenia w swojej sekcji Zgłoszeń.
---
 
### Admin
 
* **US16**: Jako admin chcę przeglądać listę wszystkich użytkowników systemu, aby mieć pełną kontrolę nad systemem.
  * **Kryteria akceptacyjne:**
    - widzę listę wszystkich kont z ich Rolami i statusem aktywności.
* **US17**: Jako admin chcę ręcznie dodawać nowych użytkowników systemu, aby tworzyć konta z pożądanymi Rolami.
  * **Kryteria akceptacyjne:**
    - nowy użytkownik systemu może zalogować się na konto z wykorzystaniem podanych przeze mnie danych.
* **US18**: Jako admin chcę dezaktywować konta użytkowników systemu, aby blokować szkodliwych lub nieaktywnych użytkowników bez utraty ich danych historycznych.
  * **Kryteria akceptacyjne:**
    - zdezaktywowany użytkownik nie może się zalogować,
    - jego Recenzje i Zgłoszenia pozostają widoczne w systemie (dane historyczne zachowane),
    - konto widnieje na liście adminów jako nieaktywne (`active = false`).
  > **Decyzja architektoniczna:** Zamiast twardego usunięcia konta (jak w oryginalnym US12)
  > stosujemy **miękkie usunięcie** (`active = false`). Twarde usunięcie powodowałoby
  > osierocone rekordy w Interaction Context i Verification Context (naruszenie spójności
  > soft FK w architekturze EDA).
* **US19**: Jako admin chcę nadawać Role użytkownikom systemu, aby awansować Użytkowników na Edytorów lub Edytorów na Adminów.
  * **Kryteria akceptacyjne:**
    - użytkownik otrzymuje dostęp do funkcji przypisanych do nowej Roli.
* **US20**: Jako admin chcę odbierać Role użytkownikom systemu, aby degradować Adminów do Edytorów lub Edytorów do Użytkowników.
  * **Kryteria akceptacyjne:**
    - użytkownik traci dostęp do funkcji przypisanych do poprzedniej Roli.

---
 
## 5. Reguły Biznesowe (Business Rules)
 
| ID | Reguła |
|---|---|
| BR1 | Krasnal jest widoczny na mapie publicznej tylko wtedy, gdy jego status to `ACTIVE`. |
| BR2 | Jeden użytkownik może wystawić tylko jedną Recenzję dla danego Krasnala. |
| BR3 | Jeden użytkownik może mieć dany Krasnal tylko raz na swojej liście odwiedzonych. |
| BR4 | Zgłoszenie po akceptacji emituje wydarzenie, które tworzy nowego Krasnala w systemie. |
| BR5 | Odrzucenie Zgłoszenia wymaga podania powodu. |
| BR6 | Użytkownik może zgłosić nowego Krasnala tylko wtedy, kiedy jest zalogowany. |
| BR7 | Edycja Krasnali i weryfikacja Zgłoszeń wymaga Roli co najmniej `EDITOR`. |
| BR8 | Zarządzanie kontami i Rolami wymaga Roli `ADMIN`. |
| BR9 | Usunięcie konta realizowane jest jako wskazanie konta jako nieaktywne. |
| BR10 | Ocena musi być liczbą całkowitą z zakresu 1–5. |
