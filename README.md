# 1. Zarządzanie książkami
Zarządzanie Książkami (Book Manager) - aplikacja desktopowa umożliwiająca zarządzanie księgozbiorem. Aplikacja oferuje interfejs graficzny z tabelą prezentującą książki, umożliwia wyszukiwanie, dodawanie, filtrowanie i usuwanie książek z bazy danych. 

## Spis treści
1. [Opis projektu](#opis-projektu)
2. [Funkcjonalności](#funkcjonalności)
3. [Instrukcje obsługi](#instrukcje-obsługi)
    - [Wymagania wstępne](#wymagania-wstępne)
    - [Instalacja](#instalacja)
    - [Uruchomienie aplikacji](#uruchomienie-aplikacji)
4. [Kontakt](#kontakt)

## Opis projektu
Projekt "Zarządzanie książkami" jest aplikacją w jęzzyku Java z graficznym interfejsem użytkownika (GUI), która pozwala na zarządzanie listą książek. Aplikacja umożliwia dodawanie, usuwanie, wyszukiwanie oraz wyświetlanie szczegółowych informacji o książkach. Dane są przechowywane w bazie danych SQLite, i są zapamiętywane po zamknięciu aplikacji.

## Funkcjonalności
- **Dodawanie książek**: Umożliwia dodanie nowej książki do bazy danych poprzez formularz.
- **Usuwanie książek**: Pozwala na usunięcie wybranej książki z listy.
- **Filtrowanie zbiortu**: Klikając na nagłówek danej kolumny możemy przefiltrować zbiór.
- **Wyszukiwanie książek**: Umożliwia wyszukiwanie książek na podstawie dowolnego pola (tytuł, autor, rok, gatunek) za pomocą wyrażeń regularnych.
- **Wyświetlanie informacji o książce**: Wyświetla szczegółowe informacje o wybranej książce w oknie dialogowym.
- **Półprzeźroczyste tło**: Aplikacja posiada graficzny interfejs z półprzeźroczystym tłem, które jest obrazem `background.jpg`.

## Instrukcje obsługi
Najważniejsze pliki to BookManager.java, books.db, background.jpg oraz plik sqlite-jdbc w katalogu "lib". Obecnie projekt posiada skompilowane pliki klas, więc teoretycznie powinieneś móc uruchomić program klikając "run" w swoim IDE od razu po jego pobraniu. Gdyby jednak z jakiś wzglęgów program nie uruchamiał się sprawdź poniższe kroki!

W projekcie znajduje się uzupełniona baza danych 25 pozycjami - plik books.db. W razie potrzeby usuń pozycje i dodaj swoje zbiory z półki.

### Wymagania wstępne
- Java Development Kit (JDK) 21.
- Biblioteka SQLite JDBC: [sqlite-jdbc-3.36.0.3.jar](https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.36.0.3/sqlite-jdbc-3.36.0.3.jar)

### Instalacja
1. Pobierz i zainstaluj JDK z [oficjalnej strony Oracle](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) (jeśli jeszcze nie masz zainstalowanego).
2. Pobierz bibliotekę SQLite JDBC i umieść ją w katalogu projektu, np. w folderze `lib`.

### Uruchomienie aplikacji

#### macOS/Linux
1. Skompiluj plik `BookManager.java`:
   ```sh
   javac -cp .:lib/sqlite-jdbc-3.36.0.3.jar BookManager.java
#### Windows
javac -cp .;lib/sqlite-jdbc-3.36.0.3.jar BookManager.java


## Kontakt
Zapraszam na mojego github https://github.com/SecDev7