# Dziedziny
## Główna

* **Mapowanie położenia obiektów**  
Głównym aspektem projektu jest przechowywanie podstawowych informacji o *Krasnalach* i obrazowanie ich pozycji na mapie. W tym celu przechowywane są dane geolokalizacyjne każdego z obiektów.

## Wspierające

* **Dodawanie komentarzy do obiektów**  
Dodatkowy aspekt projektu rozszerzający dostępne dane o *Krasnalach*. Użytkownicy aplikacji mają możliwość komentować i oceniać już odwiedzone obiekty w systemie.
* **Weryfikacja nowych zgłoszeń dodania obiektów**  
Dodatkowy aspekt projektu pozwalający użytkownikom na dodawanie nowych *Krasnali*. Po poprawnego dodania obiektu do aplikacji niezbędna jest weryfikacja administratora.
* **Kategoryzacja krasnalo-podobnych obiektów**  
Dodatkowy aspekt projektu pozwalający na filtrowanie i kategoryzowanie *Krasnali*. Użytkownik ma możliwość dodawać *Krasnale* do prywatnie stworzonych kategorii. Dodatkowo do każdego obiektu przypisano filtry pozwalające na sprawne wyszukiwanie podobnych *Krasnali*.

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
Dodawanie komentarzy i opinii do każdego *Krasnala*

## Weryfikacja nowych zgłoszeń dodania obiektów

* **Kontekst zgłoszeń**  
Dodawanie zgłoszeń przez użytkowników aplikacji w celu dodania nowych *Krasnali* do systemu. Mozliwość akceptacji albo odrzucenia nowych zgłoszeń przez administratora.

## Kategoryzacja

* **Kontekst kategoryzacji lokalnej i globalnej**
Dodanie fitrów globalnych i lokalnych. Globalne filtry są stale przypisane do każdego *Krasnala*. Lokalne filtry są tworzone przez każdego użytkownika osobno.

## Zarządzanie tożsamością i dostępem do danych

* **Kontekst logowania**  
Możliwość zalogowania się do systemu i uzyskania odpowiednich permiscji.
* **Kontekst zarządzania użytkownikami**  
Dodawanie lub usuwanie dodatkowych permisji dla kont użytkowników. Możliwość ręcznego usuwania lub dodawania nowych użytkowników.

# Język wszechobecny

* **Krasnal** - Obiekt świata rzeczywistego zobrazowany na mapie przez system. Posiada dodatkowe pola pozwalające poznać historię obiektu i przeglądać sekcję komentarzy. Dodatkowo każdy obiekt posiada przypisane kategorie.
* **Zgłoszenie** - Propozycja dodania nowego *Krasnala* do systemu.
* **Komentarz** - Tekstowa adnotacja napisana przez użytkownika odnosząca się do konkretnego krasnala
* **Kategoria (filtr globalny)** - znacznik klasyfikujący *Krasnala* (budynek, zabytek, krasnal, flora, miejsce).
* **Weryfikacja** - proces zatwierdzenia zgłoszenia przez administratora. Możliwe jest zaakceptowanie, edycja lub odrzucenie.
* **Sekcja odwiedzonych (filtr lokalny)** - znacznik pozwalający odróżnić *Krasnale*. Dodawany przez użytkownika aplikacji.
* **Permicja** - uprawnienie danej z ról w systemie.
* **Użytkownik systemu** - (Nie mylić z rolą użytkownik) osoba posiadając konto w systemie.

## Role

* **Gość** - Niezalogowana osoba do systemu. Posiada możliwość obejrzeć mapę *Krasnali* i wyszukać *Krasnale* wykorzystując kategorie.
* **Użytkownik** - Zalogowana osoba. Posiada możliwość dodawania komentarzy, oceny *Krasnali* i dodawania ich do sekcji odwiedzonych. Dodatkowo ma możliwość wysłać zgłoszenie dodania nowego obiektu do systemu.
* **Edytor** - Zalogowana osoba z uprawnieniami edytorskimi. Może edytować obiekty krasnali oraz weryfikować zgłoszenia użytkowników.
* **Admin** - Najważniejsza osoba w systemie. Posiada pełną władzę nad dostępnymi danymi w systemie. Ma możliwość dodawać i usuwać użytkowników. Ma możliwość dodawać i usuwać permisje użytkownikom systemu.

> Każda rola dzieli uprawnienia z rolami wyżej wymienionymi.

