# Wrocławskie Krasnale
> System mapowania punktów użyteczności publicznej - POI (ang. Point of interest) utrzymywany przez społeczność
 
## Dziedziny
### Główna

* **Mapowanie położenia obiektów**  
Głównym aspektem projektu jest przechowywanie podstawowych informacji o *Krasnalach* i obrazowanie ich pozycji na mapie. W tym celu przechowywane są dane geolokalizacyjne każdego z obiektów. *Krasnale* mają przypisane kategorie, wedle których można filtrować ich obecność na mapie. Użytkownik ma możliwość dodawania *Krasnali* do indywidualnych kategorii, m.in. listy odwiedzonych *Krasnali*.

### Wspierające

* **Weryfikacja nowych zgłoszeń dodania obiektów**  
Dodatkowy aspekt projektu pozwalający użytkownikom na dodawanie nowych *Krasnali*. Po poprawnego dodania obiektu do aplikacji niezbędna jest weryfikacja administratora.

* **Dodawanie komentarzy i ocen do obiektów**  
Dodatkowy aspekt projektu rozszerzający dostępne dane o *Krasnalach*. Użytkownicy aplikacji mają możliwość komentować i oceniać już odwiedzone obiekty w systemie.

### Ogólna

* **Zarządzanie tożsamością i dostępem do danych (IAM)**  
Ogólny aspekt projektu pzowajalający na rejestrację, logowanie i zarządzanie Rolami użytkowników systemu.

---

## Konteksty

### * **Kontekst Krasnal**  
Przechowywanie i manipulowanie informacjami dotyczącymi *Krasnali*: współrzędnymi, nazwą, opisem, kategorią i statusem. 

~~* **Kontekst Mapowania (zewnętrzny)**
Obrazowanie wykorzystując zewnętrzny system obiektów na mapie.~~

###* **Kontekst Zgłoszenie**
Kolejka propozycji nowych *Krasnali* przesyłanych przez użytkowników, oczekujących
na akceptację lub odrzucenie przez Edytora bądź Admina.

### * **Kontekst Interakcja**  
Komentarze i oceny wystawiane przez użytkowników dla *Krasnali* oraz prywatna lista
obiektów oznaczonych przez użytkownika jako odwiedzone.

### * **Kontekst IAM (ang. Identity and Access Management)**  
Rejestracja, uwierzytelnianie (logowanie) oraz autoryzacja (zarządzanie Rolami
użytkowników systemu). Stanowi jedyny kontekst, który zna tożsamość użytkownika.

## 6. Język Wszechobecny (Ubiquitous Language)
 
### 6.1 Kontekst Krasnali — *POI Catalog Context*
 
| Termin PL | Termin EN | Definicja |
|---|---|---|
| Krasnal | *Dwarf* | Obiekt świata rzeczywistego zobrazowany na mapie. Posiada nazwę, opis, współrzędne, kategorię i status. |
| Nazwa | *name* | Krótka identyfikująca etykieta Krasnala (max 255 znaków). |
| Opis | *description* | Tekstowa informacja o historii i charakterystyce Krasnala. |
| Lokalizacja | *location* | Położenie geograficzne Krasnala wyrażone parą współrzędnych (szerokość, długość). |
| Współrzędne | *Coordinates* | Wartość złożona: `latitude` i `longitude` z walidacją zakresu. |
| Kategoria | *DwarfCategory* | Enum klasyfikujący Krasnala: `MONUMENT`, `BUILDING`, `DWARF_FIGURINE`, `FLORA`, `PLACE`. |
| Status | *DwarfStatus* | Enum określający widoczność Krasnala: `ACTIVE`, `INACTIVE`, `ARCHIVED`. |
 
### 6.2 Kontekst Zgłoszeń — *Verification Context*
 
| Termin PL | Termin EN | Definicja |
|---|---|---|
| Zgłoszenie | *Submission* | Propozycja dodania nowego Krasnala przesłana przez użytkownika. Zawiera wszystkie dane wymagane do stworzenia Krasnala. |
| Ładunek zgłoszenia | *SubmissionPayload* | Zestaw danych proponowanego Krasnala przechowywany jako JSONB do momentu weryfikacji. |
| Weryfikacja | *Verification* | Proces przeglądu Zgłoszenia przez Edytora lub Admina. Może zakończyć się akceptacją, edycją lub odrzuceniem. |
| Powód odrzucenia | *rejectionReason* | Obowiązkowe wyjaśnienie podawane przez weryfikującego w przypadku odrzucenia Zgłoszenia. |
| Status zgłoszenia | *SubmissionStatus* | Enum: `PENDING` (oczekuje), `ACCEPTED` (zaakceptowane), `REJECTED` (odrzucone). |
 
### 6.3 Kontekst Interakcji — *Interaction Context*
 
| Termin PL | Termin EN | Definicja |
|---|---|---|
| Komentarz | *comment* (część Review) | Tekstowa adnotacja użytkownika odnosząca się do konkretnego Krasnala (max 2000 znaków). |
| Ocena | *Rating* | Wartość całkowita od 1 do 5 wystawiana przez użytkownika dla Krasnala. |
| Recenzja | *Review* | Agregat łączący Ocenę i Komentarz — użytkownik wystawia je razem. |
| Średnia ocen | *average rating* | Wartość wyliczana na żądanie ze wszystkich Recenzji danego Krasnala. |
| Lista odwiedzonych | *visited list* | Prywatna lista Krasnali oznaczonych przez konkretnego użytkownika jako odwiedzone. |
| Wpis odwiedzenia | *VisitedEntry* | Pojedynczy rekord na liście odwiedzonych: powiązanie użytkownika z Krasnale i czas oznaczenia. |
 
### 6.4 Kontekst IAM — *IAM Context*
 
| Termin PL | Termin EN | Definicja |
|---|---|---|
| Użytkownik systemu | *SystemUser* | (Nie mylić z Rolą USER) — każda osoba posiadająca konto w systemie. |
| Rola | *UserRole* | Enum określający poziom uprawnień w systemie: `GUEST`, `USER`, `EDITOR`, `ADMIN`. |
| Rejestracja | *registration* | Proces założenia konta przez Gościa w celu uzyskania Roli `USER`. |
| Logowanie | *login* | Proces potwierdzenia tożsamości przez użytkownika systemu w celu uzyskania sesji. |
| Gość | *Guest* | Niezalogowana osoba. Rola `GUEST` — może przeglądać mapę i filtrować Krasnale. |
| Użytkownik | *User* | Zalogowana osoba z Rolą `USER` — może dodawać Recenzje, oznaczać Krasnale jako odwiedzone i zgłaszać nowe. |
| Edytor | *Editor* | Zalogowana osoba z Rolą `EDITOR` — może edytować Krasnale i weryfikować Zgłoszenia. |
| Admin | *Admin* | Najwyższy poziom uprawnień. Posiada wszystkie uprawnienia Edytora, plus zarządzanie kontami i Rolami. |

---

# Język wszechobecny

## Kontekst Krasnali
* **Krasnal | *Dwarf*** - Obiekt świata rzeczywistego zobrazowany na mapie przez system. Posiada dodatkowe pola pozwalające poznać historię obiektu i przeglądać sekcję komentarzy. Dodatkowo każdy obiekt posiada przypisane kategorie.
* **Kategoria (filtr globalny) | *category (global filter)*** - Znacznik klasyfikujący *Krasnala* (budynek, zabytek, krasnal, flora, miejsce).
* **Lokalizacja | *location*** - Położenie *Krasnala* na mapie, na podstawie danych geolokalizacyjnych.
* **Lista odwiedzonych | *visited list*** - Indywidualna lista użytkownika, na której znajdują się *Krasnale* z przypisaną przez użytkownika kategorią odwiedzonego *Krasnala*.

## Kontekst mapowania (zewnętrzne API)
* **Punkt mapowy | *map point*** - Reprezentacja lokalizacji *Krasnala* w systemie mapowym.
* **Obszar | *area*** - Zakres współrzędnych wykorzystywanych przez system.

## Kontekst komentarzy
* **Komentarz | *comment*** - Tekstowa adnotacja napisana przez użytkownika odnosząca się do konkretnego *Krasnala*.
* **Ocena | *rating*** - Adnotacja odnosząca się do konkretnego *Krasnala* wyrażana liczbą od 1 do 5, zostawiana przez użytkownika. Użytkownik widzi średnią ocen zostawionych przez wszystkich użytkowników.

# Kontekst zgłoszeń
* **Zgłoszenie | *report*** - Propozycja dodania nowego *Krasnala* do systemu.
* **Weryfikacja | *verification*** - proces zatwierdzenia zgłoszenia przez administratora. Możliwe jest zaakceptowanie, edycja lub odrzucenie.

# Kontekst logowania / Kontekst zarządzania tożsamością i dostępem do danych
* **Permisja | *permission*** - uprawnienie danej z ról w systemie.
* **Użytkownik systemu | *system user*** - (Nie mylić z rolą użytkownik) osoba posiadając konto w systemie.
* **Logowanie | *login*** - proces potwierdzenia tożsamości w systemie przez gościa w celu uzyskania uprawnień użytkownika.
* **Rejestracja | *registration*** - proces dodania swojej tożsamości do systemu przez gościa w celu uzyskania uprawnień użytkownika.

## Role

* **Gość | *guest*** - Niezalogowana osoba do systemu. Posiada możliwość obejrzeć mapę *Krasnali* i wyszukać *Krasnale* wykorzystując kategorie.
* **Użytkownik | *user*** - Zalogowana osoba. Posiada możliwość dodawania komentarzy, oceny *Krasnali* i dodawania ich do sekcji odwiedzonych. Dodatkowo ma możliwość wysłać zgłoszenie dodania nowego obiektu do systemu.
* **Edytor | *editor*** - Zalogowana osoba z uprawnieniami edytorskimi. Może edytować obiekty krasnali oraz weryfikować zgłoszenia użytkowników.
* **Admin** - Najważniejsza osoba w systemie. Posiada pełną władzę nad dostępnymi danymi w systemie. Ma możliwość dodawać i usuwać użytkowników. Ma możliwość dodawać i usuwać permisje użytkownikom systemu.

> Każda rola dzieli uprawnienia z rolami wyżej wymienionymi.

# Historyjki użytkownika

## Gość
* US1: Jako gość chcę oglądać mapę *Krasnali*, aby wiedzieć, gdzie mogę znaleźć atrakcje we Wrocławiu.
* US2: Jako gość chcę filtrować *Krasnale* na mapie na podstawie kategorii, aby widzieć jedynie te obiekty, które mnie interesują.
* US3: Jako gość chcę wyświetlać informacje o *Krasnalach* (nazwa, opis, kategoria, średnia ocen, komentarze), aby zapoznać się z ich charakterystyką oraz opiniami użytkowników.
* US4: Jako gość chcę zarejestrować się do systemu, aby uzyskać rolę użytkownika i móc korzystać z funkcji dostępnych tylko dla zalogowanych użytkowników.

## Użytkownik
~~* US5: Jako użytkownik chcę logować się do systemu, aby uzyskać dostęp do swojej sekcji odwiedzonych *Krasnali* oraz móc dodawać opinie na ich temat i zgłaszać nowe *Krasnale*.~~
* US5: Jako użytkownik chcę uzyskać dostęp do swojej sekcji odwiedzonych *Krasnali* oraz móc dodawać opinie na ich temat i zgłaszać nowe *Krasnale*.
* US6: Jako użytkownik chcę dodawać komentarze i oceny do *Krasnali*, aby pozostali użytkownicy systemu i goście znali moją opinię.
* US7: Jako użytkownik chcę dodawać *Krasnale* do swojej sekcji odwiedzonych, aby śledzić odwiedzone atrakcje oraz znajdować te, których jeszcze nie odwiedziłem.
* US8: Jako użytkownik chcę zgłaszać nowe *Krasnale*, aby mapa była aktualna i uzupełniona o większą ilość informacji.

## Edytor
* US9: Jako edytor chcę edytować dane *Krasnali*, aby opisy na ich temat oraz kategorie były aktualne.
* US10: Jako edytor chcę weryfikować zgłoszenia nowych *Krasnali*, aby zapewnić poprawność ich danych podanych przez użytkowników.

## Admin
* US11: Jako admin chcę dodawać nowych użytkowników systemu, aby tworzyć użytkowników systemu z pożądanymi przeze mnie uprawnieniami.
* US12: Jako admin chcę usuwać użytkowników systemu, aby pozbywać się szkodliwych lub nieaktywnych użytkowników.
* US13: Jako admin chcę dodawać permisje użytkowników systemu, aby móc awansować użytkowników na edytorów, bądź edytorów na adminów.
* US14: Jako admin chcę usuwać permisje użytkowników systemu, aby móc degradować adminów do edytorów bądź edytorów do użytkowników.
