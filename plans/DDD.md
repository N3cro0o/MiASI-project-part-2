# Dziedziny
## Główna

* **Mapowanie położenia obiektów**  
Głównym aspektem projektu jest przechowywanie podstawowych informacji o *Krasnalach* i obrazowanie ich pozycji na mapie. W tym celu przechowywane są dane geolokalizacyjne każdego z obiektów. *Krasnale* mają przypisane kategorie, wedle których można filtrować ich obecność na mapie. Użytkownik ma możliwość dodawania *Krasnali* do indywidualnych kategorii, m.in. listy odwiedzonych *Krasnali*.

## Wspierające

* **Dodawanie komentarzy do obiektów**  
Dodatkowy aspekt projektu rozszerzający dostępne dane o *Krasnalach*. Użytkownicy aplikacji mają możliwość komentować i oceniać już odwiedzone obiekty w systemie.
* **Weryfikacja nowych zgłoszeń dodania obiektów**  
Dodatkowy aspekt projektu pozwalający użytkownikom na dodawanie nowych *Krasnali*. Po poprawnego dodania obiektu do aplikacji niezbędna jest weryfikacja administratora.

## Ogólna

* **Zarządzanie tożsamością i dostępem do danych**  
Ogólny aspekt projektu pzowajalający na minipulację stanem użytkowników i na nadawnie lub usuwanie ich poszczególnych ról.

# Konteksty
## Mapowanie położenia obiektów

* **Kontekst krasnali**  
Przechowywanie i manipulowanie informacjami dotyczących *Krasnali*. 

* **Kontekst mapowania (zewnętrzne API)**  
Obrazowanie wykorzystując zewnętrzny system obiektów na mapie. 

## Dodawanie komentarzy do obiektów

* **Kontekst komentarzy**  
Dodawanie komentarzy i ocen do każdego *Krasnala*

## Weryfikacja nowych zgłoszeń dodania obiektów

* **Kontekst zgłoszeń**  
Dodawanie zgłoszeń przez użytkowników aplikacji w celu dodania nowych *Krasnali* do systemu. Mozliwość akceptacji albo odrzucenia nowych zgłoszeń przez administratora.

## Zarządzanie tożsamością i dostępem do danych

* **Kontekst logowania**  
Możliwość zalogowania się do systemu i uzyskania odpowiednich permisji.
* **Kontekst zarządzania użytkownikami**  
Dodawanie lub usuwanie dodatkowych permisji dla kont użytkowników. Możliwość ręcznego usuwania lub dodawania nowych użytkowników.

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
* **US1**: Jako gość chcę oglądać mapę *Krasnali*, aby wiedzieć, gdzie mogę znaleźć atrakcje we Wrocławiu.
  * **Kryteria akceptacyjne**:
    - na mapie wyświetlają się *Krasnale*,
    - *Krasnale* nie znajdują się poza obszarem Wrocławia
* **US2**: Jako gość chcę filtrować *Krasnale* na mapie na podstawie kategorii, aby widzieć jedynie te obiekty, które mnie interesują.
  * **Kryteria akceptacyjne**:
    - wyświetlane na mapie *Krasnale* mają przypisaną jedną z wybranych przeze mnie kategorii
* **US3**: Jako gość chcę wyświetlać informacje o *Krasnalach*, aby zapoznać się z ich opisem oraz opiniami użytkowników.
  * **Kryteria akceptacyjne**:
    - widoczny jest opis *Krasnala*,
    - widoczne są opinie użytkowników wraz z nazwą użytkownika,
    - widoczna jest średnia ocen wszystkich użytkowników
* **US4**: Jako gość chcę zarejestrować się do systemu, aby uzyskać rolę użytkownika i móc korzystać z funkcji dostępnych tylko dla zalogowanych użytkowników.
  * **Kryteria akceptacyjne**:
    - otrzymuję dostęp do funkcji dostępnych tylko dla zalogowanych użytkowników

## Użytkownik
* **US5**: Jako użytkownik chcę logować się do systemu, aby uzyskać dostęp do swojej sekcji odwiedzonych *Krasnali* oraz móc dodawać opinie na ich temat i zgłaszać nowe *Krasnale*.
  * **Kryteria akceptacyjne**:
    - sekcja moich odwiedzonych *Krasnali* nie zmieniła się względem ostatniego razu, gdy korzystałem z aplikacji,
    - mogę dodawać komentarze oraz oceny do *Krasnali*,
    - mogę dodać zgłoszenie nowego *Krasnala*
* **US6**: Jako użytkownik chcę dodawać komentarze i oceny do *Krasnali*, aby pozostali użytkownicy systemu i goście znali moją opinię.
  * **Kryteria akceptacyjne**:
    - widzę swoją ocenę *Krasnala* w sekcji wystawionych przeze mnie ocen,
    - widzę swój komentarz na temat *Krasnala* w sekcji napisanych przeze mnie komentarzy,
    - pozostali goście i użytkownicy widzą mój komentarz
* **US7**: Jako użytkownik chcę dodawać *Krasnale* do swojej sekcji odwiedzonych, aby śledzić odwiedzone atrakcje oraz znajdować te, których jeszcze nie odwiedziłem.
  * **Kryteria akceptacyjne**:
    - w mojej sekcji odwiedzonych *Krasnali* znajduje się właśnie dodany do niej *Krasnal*,
    - na mapie mogę filtrować odwiedzone *Krasnale*, aby wyświetlały się tylko te nieodwiedzone
* **US8**: Jako użytkownik chcę zgłaszać nowe *Krasnale*, aby mapa była aktualna i uzupełniona o większą ilość informacji.
  * **Kryteria akceptacyjne**:
    - zgłoszenie *Krasnala* jest widoczne w mojej sekcji zgłoszeń

## Edytor
* **US9**: Jako edytor chcę edytować dane *Krasnali*, aby opisy na ich temat oraz kategorie były aktualne.
  * **Kryteria akceptacyjne**:
    - w sekcji *Krasnala* widoczny jest jego nowy opis lub kategoria
* **US10**: Jako edytor chcę weryfikować zgłoszenia nowych *Krasnali*, aby zapewnić poprawność ich danych podanych przez użytkowników.
  * **Kryteria akceptacyjne**:
    - po pozytywnej weryfikacji zgłoszenia nowy *Krasnal* pojawia się na mapie,
    - w przypadku odrzucenia zgłoszenia przestaje się ono pojawiać w sekcji zgłoszeń użytkownika, który je wykonał

## Admin
* **US11**: Jako admin chcę dodawać nowych użytkowników systemu, aby tworzyć użytkowników systemu z pożądanymi przeze mnie uprawnieniami.
  * **Kryteria akceptacyjne**:
    - użytkownik systemu może zalogować się na konto z wykorzystaniem podanych przeze mnie danych
* **US12**: Jako admin chcę usuwać użytkowników systemu, aby pozbywać się szkodliwych lub nieaktywnych użytkowników.
  * **Kryteria akceptacyjne**:
    - konto użytkownika systemu nie istnieje i nie można się na nie zalogować
* **US13**: Jako admin chcę dodawać permisje użytkowników systemu, aby móc awansować użytkowników na edytorów, bądź edytorów na adminów.
  * **Kryteria akceptacyjne**:
    - użytkownik otrzymuje dostęp do funkcji edytora, bądź edytor dostaje dostęp do funkcji admina
* **US14**: Jako admin chcę usuwać permisje użytkowników systemu, aby móc degradować adminów do edytorów bądź edytorów do użytkowników.
  * **Kryteria akceptacyjne**:
    - edytor traci swoje funkcje i może jedynie korzystać z funkcji użytkownika, lub admin traci możliwość korzystania z funkcji dostępnych wyłącznie dla jego roli
