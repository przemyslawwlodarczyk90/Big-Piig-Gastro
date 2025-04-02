# 🐷 Big Piig Gastro - Platforma dla HoReCa 🍽️

Big Piig Gastro to sklep internetowy zaprojektowany specjalnie dla branży HoReCa. Platforma umożliwia zarządzanie produktami, kategoriami, zamówieniami oraz dostęp do danych pogodowych i wsparcie AI przy planowaniu zakupów.

---

## 🚀 Główne funkcje

- 🛒 **Zarządzanie produktami**: Dodawanie, edycja, usuwanie produktów, przypisywanie do kategorii i producentów.
- 🗂️ **Kategorie**: Tworzenie i edycja hierarchii kategorii produktów.
- 📦 **Zamówienia**: Przeglądanie i aktualizacja statusów zamówień.
- 🔐 **Autentykacja i autoryzacja**: Role użytkowników (administrator, klient).
- 🧺 **Koszyk**: Dodawanie produktów do koszyka i finalizacja zamówień.
- ☁️ **Pogoda**: Integracja z API pogodowym.
- 🧠 **Asystent AI**: Tworzenie spersonalizowanych list zakupów na podstawie preferencji użytkownika (rodzaj kuchni, budżet, wielkość lokalu, estetyka itp.).

---

## 🚀 Technologie

- ☕ Spring Boot
- ⛓️ Spring Security
- 📃 Thymeleaf
- 📆 PostgreSQL
- ⚖️ JPA & Hibernate
- 🌐 Bootstrap

---

## 🔧 Dokumentacja API

Aplikacja zawiera dokumentację API w Swagger UI, dostępną po uruchomieniu aplikacji pod adresem:

[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

## 🔬 Testowanie

- ✅ **Testy jednostkowe**: Modele zamówień, produktów, kategorii.
- 🚫 **Testy integracyjne**: Integracja komponentów, DTO.
- 🧪 **Mockowanie zależności**: Mockito.

Testy zapewniają niezawodność i wysoką jakość działania systemu.

---

## 🚧 Uruchomienie projektu

### Wymagania

- Java 11+
- Maven
- PostgreSQL

### Krok po kroku

1. 💾 Sklonuj repozytorium.
2. 🔑 Skonfiguruj dane dostępowe do bazy danych w `application.properties`.
3. 📂 Zbuduj: `mvn clean install`
4. 🚀 Uruchom: `mvn spring-boot:run`
5. 🏠 Wejdź na: [http://localhost:8080](http://localhost:8080)

---

## 📄 Licencja

Projekt udostępniany na licencji **MIT**. Możesz z niego korzystać komercyjnie i prywatnie.

---

Z miłości do gastronomii i technologii ❤️

**Big Piig Dev Team**
