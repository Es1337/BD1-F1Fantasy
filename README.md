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
![Alt text][https://i.imgur.com/wrFHW7W.jpg]

## III. Projekt logiczny
* 7. Projektowanie tabel, kluczy, indeksów
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