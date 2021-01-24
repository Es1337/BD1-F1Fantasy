Java/PostgresQL app source code. Below is documentation in Polish:

# Projekt na Bazy Danych 1 - F1 Fantasy League

## I. Projekt koncepcji, założenia
* 1. Zdefiniowanie tematu projektu  
    Celem projektu jest stworzenie aplikacji bazodanowej, która obsługuje grę typu Fantasy League opartą na Formule 1.
* 2. Analiza wynagań użytkownika  
    Aplikacja ma :
        - Obsługiwać logowanie i rejestrację
        - Wyświetlać harmonogram wyścigów(miejsca, daty, godziny)
        - Wyświetlać aktualną tabele wyników
        - Wyświetlać ranking użytkowników
        - Pozwalać na wprowadzanie przewidywań
        - Obsługiwać wprowadzanie danych przez moderatora

## II. Projekt dragramów (konceptualny)
* 4. Budowa i analiza diagramu przepływu danych DFD  
Baza danych w projekcie umożliwa użytkownikom na czytanie danych z tabel, a tym którzy mają uprawnienia administratora na wprowadzanie, więc diagram przepływu danych nie ma praktycznego zastosowania.
* 5. Zdefiniowanie encji oraz ich atrybutów,  Zaprojektowanie relacji pomiędzy encjami (Diagram ERD)
![Alt text](https://i.imgur.com/wrFHW7W.jpg)

## III. Projekt logiczny
* 7. Projektowanie tabel, kluczy, indeksów  
<p>

|           | season  |                                            |  
|-----------|---------|--------------------------------------------|  
| Kolumna   | Typ     | Opis                                       |  
| season_id | INTEGER | Klucz główny                               |  
| year      | VARCHAR | Unikalny rok w którym odbywa się sezon     |  
| wdc       | VARCHAR | Imie i nazwisko Mistrza Świata Kierowców   |  
| wcc       | VARCHAR | Nazwa zespołu Mistrza Świata Konstruktorów |  
<p>

|           | teams   |                                              |
|-----------|---------|----------------------------------------------|
| Kolumna   | Typ     | Opis                                         |
| team_id   | INTEGER | Klucz główny                                 |
| season_id | INTEGER | ID sezonu w którym zapisany jest zespół [FK] |
| name      | VARCHAR | Nazwa zespołu                                |
| points    | INTEGER | Aktualna liczba punktów                      |
<p>

|           | drivers |                                                |
|-----------|---------|------------------------------------------------|
| Kolumna   | Typ     | Opis                                           |
| driver_id | INTEGER | Klucz główny                                   |
| team_id   | INTEGER | ID zespołu do którego należy kierowca [FK]     |
| season_id | INTEGER | ID sezonu w którym zapisany jest kierowca [FK] |
| fname     | VARCHAR | Imię kierowcy                                  |
| lname     | VARCHAR | Nazwisko kierowcy                              |
| points    | INTEGER | Aktualna liczba punktów                        |
<p>

|           | races     |                                                          |
|-----------|-----------|----------------------------------------------------------|
| Kolumna   | Typ       | Opis                                                     |
| race_id   | INTEGER   | Klucz główny                                             |
| season_id | INTEGER   | ID sezonu w którym zapisany jest wyścig [FK]             |
| location  | VARCHAR   | Lokalizacja Grand Prix np. GP Niemiec                    |
| track     | VARCHAR   | Nazwa toru na którym rozgrywane jest Grand Prix          |
| fp1       | TIMESTAMP | Data i czas startu FP1(yyyy-mm-dd hh:mm:ss) UTC          |
| fp2       | TIMESTAMP | Data i czas startu FP2(yyyy-mm-dd hh:mm:ss) UTC          |
| fp3       | TIMESTAMP | Data i czas startu FP3(yyyy-mm-dd hh:mm:ss) UTC          |
| quali     | TIMESTAMP | Data i czas startu kwalifikacji(yyyy-mm-dd hh:mm:ss) UTC |
| race      | TIMESTAMP | Data i czas startu wyścigu(yyyy-mm-dd hh:mm:ss) UTC      |
<p>

|             | race_results |                                                         |
|-------------|--------------|---------------------------------------------------------|
| Kolumna     | Typ          | Opis                                                    |
| result_id   | INTEGER      | Klucz główny                                            |
| race_id     | INTEGER      | ID wyścigu z którego jest wynik [FK]                    |
| driver_id   | INTEGER      | ID kierowcy którego jest wynik [FK]                     |
| position    | INTEGER      | Pozycja kierowcy w wyścigu                              |
| points      | INTEGER      | Zdobyte punkty                                          |
| dnf         | INTEGER      | Czy udało się kierowcy ukończyć wyścig: TAK-0, NIE-1    |
| fastest_lap | INTEGER      | Czy kierowca uzyskał najszybsze okrążenie: TAK-1, NIE-0 |
<p>

|          | points_dict | [Słownik]         |
|----------|-------------|-------------------|
| Kolumna  | Typ         | Opis              |
| position | INTEGER     | Pozycja w wyścigu |
| points   | INTEGER     | Punkty za pozycję |
<p>

|           | ranking |                              |
|-----------|---------|------------------------------|
| Kolumna   | Typ     | Opis                         |
| user_id   | INTEGER | ID użytkownika [PFK]         |
| season_id | INTEGER | ID sezonu [PFK]              |
| points    | INTEGER | Punkty użytkownika w sezonie |
<p>

|         | user_predictions |                     |
|---------|------------------|---------------------|
| Kolumna | Typ              | Opis                |
| up_id   | INTEGER          | Klucz głowny        |
| user_id | INTEGER          | ID użytkownika [FK] |
| race_id | INTEGER          | ID wyścigu [FK]     |
| title   | VARCHAR          | Tytuł przewidywania |
<p>

|               | available_predictions |                   |
|---------------|-----------------------|-------------------|
| Kolumna       | Typ                   | Opis              |
| prediction_id | INTEGER               | Klucz głowny      |
| season_id     | INTEGER               | ID sezonu [FK]    |
| code          | VARCHAR               | Kod przewidywania |
<p>

|                  | users   |                                           |
|------------------|---------|-------------------------------------------|
| Kolumna          | Typ     | Opis                                      |
| user_id          | INTEGER | Klucz głowny                              |
| email            | VARCHAR | Nazwa użytkownika                         |
| fname            | VARCHAR | Imię użytkownika                          |
| lname            | VARCHAR | Nazwisko użytkownika                      |
| pass             | VARCHAR | Hasło                                     |
| admin_privileges | INTEGER | Czy użytkownik jest adminem: TAK-1, NIE-0 |
<p>

* 8. Słowniki danych
* 9. Analiza zależnośći funkcyjnych i normalizacja tabel
* 10. Denormalizacja struktury tabel
* 11. Zaprojektowanie operacji na danych

## IV. Projekt funkcjonalny
* 12. Interfejsy do prezentacji, edycji i obsługi danych
* 13. Wizualizacja danych
* 14. Zdefiniowane panelu sterowania aplikacji
* 15. Makropolecenia

## V. Dokumentacja
* 16. Wprwowadzanie danych
* 17. Dokumentacja użytkownika
* 18. Opracowanie dokumentacji technicznej
* 19. Wykaz literatury