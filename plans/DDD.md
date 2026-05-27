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

## Język Wszechobecny (Ubiquitous Language)
 
### Kontekst Krasnali — *POI Catalog Context*
 
| Termin PL | Termin EN | Definicja |
|---|---|---|
| Krasnal | *Dwarf* | Obiekt świata rzeczywistego zobrazowany na mapie. Posiada nazwę, opis, współrzędne, kategorię i status. |
| Nazwa | *name* | Krótka identyfikująca etykieta Krasnala (max 255 znaków). |
| Opis | *description* | Tekstowa informacja o historii i charakterystyce Krasnala. |
| Lokalizacja | *location* | Położenie geograficzne Krasnala wyrażone parą współrzędnych (szerokość, długość). |
| Współrzędne | *Coordinates* | Wartość złożona: `latitude` i `longitude` z walidacją zakresu. |
| Kategoria | *DwarfCategory* | Enum klasyfikujący Krasnala: `MONUMENT`, `BUILDING`, `DWARF_FIGURINE`, `FLORA`, `PLACE`. |
| Status | *DwarfStatus* | Enum określający widoczność Krasnala: `ACTIVE`, `INACTIVE`, `ARCHIVED`. |
 
### Kontekst Zgłoszeń — *Verification Context*
 
| Termin PL | Termin EN | Definicja |
|---|---|---|
| Zgłoszenie | *Submission* | Propozycja dodania nowego Krasnala przesłana przez użytkownika. Zawiera wszystkie dane wymagane do stworzenia Krasnala. |
| Ładunek zgłoszenia | *SubmissionPayload* | Zestaw danych proponowanego Krasnala przechowywany jako JSONB do momentu weryfikacji. |
| Weryfikacja | *Verification* | Proces przeglądu Zgłoszenia przez Edytora lub Admina. Może zakończyć się akceptacją, edycją lub odrzuceniem. |
| Powód odrzucenia | *rejectionReason* | Obowiązkowe wyjaśnienie podawane przez weryfikującego w przypadku odrzucenia Zgłoszenia. |
| Status zgłoszenia | *SubmissionStatus* | Enum: `PENDING` (oczekuje), `ACCEPTED` (zaakceptowane), `REJECTED` (odrzucone). |
 
### Kontekst Interakcji — *Interaction Context*
 
| Termin PL | Termin EN | Definicja |
|---|---|---|
| Komentarz | *comment* (część Review) | Tekstowa adnotacja użytkownika odnosząca się do konkretnego Krasnala (max 2000 znaków). |
| Ocena | *Rating* | Wartość całkowita od 1 do 5 wystawiana przez użytkownika dla Krasnala. |
| Recenzja | *Review* | Agregat łączący Ocenę i Komentarz — użytkownik wystawia je razem. |
| Średnia ocen | *average rating* | Wartość wyliczana na żądanie ze wszystkich Recenzji danego Krasnala. |
| Lista odwiedzonych | *visited list* | Prywatna lista Krasnali oznaczonych przez konkretnego użytkownika jako odwiedzone. |
| Wpis odwiedzenia | *VisitedEntry* | Pojedynczy rekord na liście odwiedzonych: powiązanie użytkownika z Krasnale i czas oznaczenia. |
 
### Kontekst IAM — *IAM Context*
 
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

## Historyjki użytkownika
 
### Gość
| ID | Historyjka |
|---|---|
| US1 | Jako gość chcę oglądać mapę *Krasnali*, aby wiedzieć, gdzie mogę znaleźć atrakcje we Wrocławiu. |
| US2 | Jako gość chcę filtrować *Krasnale* na mapie na podstawie kategorii, aby widzieć jedynie te obiekty, które mnie interesują. |
| US3 | Jako gość chcę wyświetlać informacje o *Krasnalach* (nazwa, opis, kategoria, średnia ocen, komentarze), aby zapoznać się z ich charakterystyką i opiniami użytkowników. |
| US4 | Jako gość chcę zarejestrować się do systemu, aby uzyskać Rolę Użytkownika i móc korzystać z funkcji dostępnych tylko dla zalogowanych. |

### Użytkownik
| ID | Historyjka |
|---|---|
| US5 | Jako użytkownik chcę logować się do systemu, aby uzyskać dostęp do swoich danych i uprawnień. |
| US6 | Jako użytkownik chcę dodawać Recenzje (komentarz + ocena) do *Krasnali*, aby pozostali użytkownicy znali moją opinię. |
| US7 | Jako użytkownik chcę usuwać własne Recenzje, aby móc zarządzać swoją aktywnością w systemie. |
| US8 | Jako użytkownik chcę dodawać *Krasnale* do swojej listy odwiedzonych, aby śledzić odwiedzone atrakcje. |
| US9 | Jako użytkownik chcę usuwać *Krasnale* ze swojej listy odwiedzonych, aby korygować swoje wpisy. |
| US10 | Jako użytkownik chcę zgłaszać nowe *Krasnale*, aby mapa była aktualna i bogatsza w informacje. |
| US11 | Jako użytkownik chcę przeglądać status moich Zgłoszeń, aby wiedzieć, czy zostały zaakceptowane lub odrzucone (wraz z powodem odrzucenia). |

### Edytor
| ID | Historyjka |
|---|---|
| US12 | Jako edytor chcę edytować dane *Krasnali* (nazwa, opis, kategoria, status), aby informacje były aktualne i poprawne. |
| US13 | Jako edytor chcę przeglądać listę oczekujących Zgłoszeń, aby podejmować decyzje weryfikacyjne. |
| US14 | Jako edytor chcę akceptować Zgłoszenia, aby nowe *Krasnale* pojawiały się na mapie. |
| US15 | Jako edytor chcę odrzucać Zgłoszenia z podaniem powodu, aby zgłaszający wiedział, dlaczego jego propozycja nie przeszła weryfikacji. |

### Admin
| ID | Historyjka |
|---|---|
| US16 | Jako admin chcę przeglądać listę wszystkich użytkowników systemu, aby mieć pełną kontrolę nad systemem. |
| US17 | Jako admin chcę ręcznie dodawać nowych użytkowników systemu, aby tworzyć konta z pożądanymi Rolami. |
| US18 | Jako admin chcę dezaktywować konta użytkowników systemu (miękkie usunięcie), aby pozbywać się szkodliwych lub nieaktywnych użytkowników bez utraty ich danych historycznych. |
| US19 | Jako admin chcę nadawać Roly użytkownikom systemu, aby awansować Użytkowników na Edytorów lub Edytorów na Adminów. |
| US20 | Jako admin chcę odbierać Role użytkownikom systemu, aby degradować Adminów do Edytorów lub Edytorów do Użytkowników. |

---
