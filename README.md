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
* 7. Słowniki danych - tabele  
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

* 8. Słowniki danych - widoki  

|                 |          | Tabela Kierowców |                                                               |
|-----------------|----------|------------------|---------------------------------------------------------------|
| Tabela źródłowa | Kolumna  | Typ              | Opis                                                          |
| -               | Pozycja  | INTEGER          | Wynik funkcji row_number() over(order by drivers.points desc) |
| drivers         | Kierowca | VARCHAR          | Imię i Nazwisko kierowcy(drivers.fname + drivers.lname)       |
| teams           | Zespół   | VARCHAR          | Nazwa zespołu (teams.name)                                   |
| drivers         | Punkty   | INTEGER          | Aktualna ilość punktów kierowcy (drivers.points)              |
<p>

|                 |         | Tabela Zespołów |                                                             |
|-----------------|---------|-----------------|-------------------------------------------------------------|
| Tabela źródłowa | Kolumna | Typ             | Opis                                                        |
| -               | Pozycja | INTEGER         | Wynik funkcji row_number() over(order by teams.points desc) |
| teams           | Zespół  | VARCHAR         | Nazwa zespołu (teams.name)                                  |
| teams           | Punkty  | INTEGER         | Aktualna ilość punktów kierowców zespołu (teams.points)     |
<p>

|                       |               | Tabela Przewidywań |                                                             |
|-----------------------|---------------|--------------------|-------------------------------------------------------------|
| Tabela źródłowa       | Kolumna       | Typ                | Opis                                                        |
| user_predictions      | Przewidywanie | VARCHAR            | Tytuł przewidywania                                         |
| available_predictions | Typ           | VARCHAR            | Kod przewidywania                                           |
| -                     | Wartość       | INTEGER            | Wartość przewidywania(wynik obliczeń z tabeli słownikowych) |
<p>

* 9. Analiza zależnośći funkcyjnych i normalizacja tabel

|     | season | teams | drivers | race_results | races | ranking | users | user_predictions | available_predictions |
|-----|--------|-------|---------|--------------|-------|---------|-------|------------------|-----------------------|
| 1NF | YES    | YES   | YES     | YES          | YES   | YES     | YES   | YES              | YES                   |
| 2NF | YES    | YES   | YES     | YES          | YES   | YES     | YES   | YES              | YES                   |
| 3NF | YES    | YES   | YES     | NO           | YES   | YES     | YES   | YES              | YES                   |
<p>

* 10. Denormalizacja struktury tabel  
Tabela race_results nie jest znormalizowana do postaci 3NF, ponieważ w obecnej postaci łatwiej odczytywać punkty zdobyte w poszczególnych wyścigach.
* 11. Zaprojektowanie operacji na danych  
Kod procedur wbudowanych jest w plikach w katalogu sql/ a kwerendy w odpowiednich klasach Javy. 

## IV. Projekt funkcjonalny
Strona główna aplikacji, po lewej pasek nawigacji po kliknięciu na poszczególne przyciski zmieniamy karty.
![Alt](https://i.imgur.com/QkZOM7v.jpeg)
Na karcie wyników wybiera się tabele oraz sezony za pomocą rozwijanych menu  
Karta wyników - Tabela zespołów
![Alt](https://i.imgur.com/PJ7wRHW.jpeg)
Karta wyników - Tabela kierowców
![Alt](https://i.imgur.com/g1DrxlN.jpeg)
Na karcie harmonogramu wybiera się sezona za pomocą rozwijanego menu, po kliknięciu na pojedynczy wiersz rozwija się dokładny rozkład weekendu wyścigowego  
Karta harmonogramu sezonu
![Alt](https://i.imgur.com/g9IXCu1.jpeg)
Na karcie przewidywań wybiera się wyścig, pierwszego kierowcę, drugiego kierowcę, zespół oraz przewidywanie specjalne dla obecnego sezonu a następnie zatwierdza przyciskiem Dodaj  
Karta przewidywań
![Alt](https://i.imgur.com/K0g36DC.jpeg)
Panel administratora - Nowy Sezon 1/4
![Alt](https://i.imgur.com/IXwLUBM.jpeg)
Panel administratora - Nowy Sezon 2/4, Dodawanie zespołów
![Alt](https://i.imgur.com/aAiiiZW.jpeg)
Panel administratora - Nowy Sezon 3/4, Dodawanie kierowców
![Alt](https://i.imgur.com/5EEIXRw.jpeg)
Panel administratora - Nowy Sezon 4/4, Dodawanie wyscigów
![Alt](https://i.imgur.com/OAwyGk5.jpeg)
Panel administratora - Nowy Wynik Wyścigu
![Alt](https://i.imgur.com/VkFJiy9.jpeg)
Panel administratora - Nadawanie uprawnień administratora
![Alt](https://i.imgur.com/2FeOsSz.jpeg)
Panel administratora - Usuwanie użytkowników
![Alt](https://i.imgur.com/5zVatcz.jpeg)
Panel administratora - Modyfikacje zespołów
![Alt](https://i.imgur.com/o00r627.jpeg)
Panel administratora - Modyfikacje kierowców
![Alt](https://i.imgur.com/BSqm1mH.jpeg)
Panel administratora - Modyfikacje wyścigów
![Alt](https://i.imgur.com/y1cwU3w.jpeg)

## V. Dokumentacja
* Wprwowadzanie danych  
    Przykładowe dane zostały wprowadzone do bazy przez autora, są to rzeczywiste dane z historycznych sezonów Formuły 1. Dane do bazy wprowadzane są ręcznie przez odpowiednie interfejsy w Panelu Administratora
* Dokumentacja użytkownika  
    Sposoby korzystania z aplikacji zostały przedstawione w projekcie funkcjonalnym
* Opracowanie dokumentacji technicznej  
    Dokumentacja znajduję się w plikach źrółowych w formacie javadoc
