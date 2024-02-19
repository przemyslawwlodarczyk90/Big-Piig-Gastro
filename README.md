# Big Piig Gastro

Big Piig Gastro to sklep internetowy zaprojektowany specjalnie dla branży HoReCa.  Platforma umożliwia zarządzanie produktami, kategoriami, zamówieniami oraz dostęp do danych pogodowych.

## Główne funkcje

- **Zarządzanie produktami**: Możliwość dodawania, edycji i usuwania produktów oraz przypisywania ich do kategorii i producentów.
- **Zarządzanie kategoriami**: Tworzenie i edycja hierarchii kategorii produktów.
- **Zarządzanie zamówieniami**: Przeglądanie i aktualizacja statusów zamówień.
- **Autentykacja i autoryzacja**: Dostosowane role dostępu dla różnych użytkowników (administrator, klient).
- **Koszyk zakupowy**: Dodawanie produktów do koszyka i finalizacja zamówień.
- **Integracja z API pogodowym**: Wyświetlanie aktualnych danych pogodowych.

## Technologie

- **Spring Boot**
- **Spring Security**
- **Thymeleaf**
- **PostgreSQL**
- **JPA & Hibernate**
- **Bootstrap**

## Testowanie

Projekt obejmuje kompleksowe testy jednostkowe i integracyjne, zapewniające wysoką jakość i niezawodność funkcjonalności:

- **Testy jednostkowe modeli**: Weryfikacja logiki biznesowej zamówień, produktów i kategorii.
- **Testy integracyjne**: Sprawdzenie integracji między komponentami, w tym reakcji na zmiany statusów zamówień oraz poprawności konwersji encji na DTO.
- **Mockowanie zależności**: Użycie Mockito do izolacji testowanych komponentów od zależności.

Te testy gwarantują, że wszystkie kluczowe funkcje systemu działają zgodnie z oczekiwaniami i są odporne na najczęstsze problemy.

## Uruchomienie projektu

### Wymagania

- Java 11 lub nowsza
- Maven
- PostgreSQL

### Krok po kroku

1. Sklonuj repozytorium z GitHub.
2. Skonfiguruj dostęp do bazy danych w `application.properties`.
3. Zbuduj aplikację komendą `mvn clean install`.
4. Uruchom aplikację przy pomocy `mvn spring-boot:run`.
5. Aplikacja będzie dostępna pod adresem `http://localhost:8080`.

## Licencja

Projekt jest udostępniany na otwartej licencji MIT, co umożliwia wykorzystanie kodu zarówno w celach prywatnych, jak i komercyjnych.
