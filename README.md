# 🐷 Big Piig Gastro - Platforma dla HoReCa 🍽️

Big Piig Gastro to sklep internetowy zaprojektowany specjalnie dla branży HoReCa. Platforma umożliwia zarządzanie produktami, kategoriami, zamówieniami oraz dostęp do danych pogodowych i wsparcie AI przy planowaniu zakupów.

---

## Logowanie JWT

Formularz `POST /user/login` (pola `username` = e-mail oraz `password`) po
poprawnej weryfikacji hasła wystawia podpisany JWT HS256 w ciasteczku
`ACCESS_TOKEN` (HttpOnly, SameSite=Lax) i przekierowuje na `/home`.
Uwierzytelnienie nie jest zapisywane w sesji; sesja nadal może służyć koszykowi
i ochronie CSRF. Role i status konta są odczytywane z bazy przy każdym żądaniu.
Niepoprawny lub wygasły token jest usuwany z przeglądarki.

Przed uruchomieniem ustaw `JWT_SECRET` na losowy sekret zapisany w Base64
(minimum 32 losowe bajty). Przykład generowania w PowerShell:

```powershell
$env:JWT_SECRET = [Convert]::ToBase64String([Security.Cryptography.RandomNumberGenerator]::GetBytes(32))
```

Zachowaj ten sam sekret między restartami, przechowując go poza repozytorium.
`JWT_TTL_SECONDS` określa ważność tokenu (domyślnie 3600 sekund).
Ciasteczko domyślnie wymaga HTTPS. Wyłącznie przy lokalnym uruchomieniu po HTTP
ustaw `JWT_COOKIE_SECURE=false`.

Formularze POST wymagają tokenu CSRF (Thymeleaf dodaje go przy `th:action`).
Wylogowanie przez `POST /logout` usuwa ciasteczko i unieważnia sesję.
Skopiowany JWT pozostaje ważny do wygaśnięcia; nie ma listy unieważnionych tokenów.

Testy logowania, ról, CSRF, podpisu i wygaśnięcia: `mvn -Dtest=JwtSecurityTest test`.

Implementacja korzysta z [obsługi JWT w Spring Security](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/jwt.html).

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

- Java 17+
- Maven
- PostgreSQL

### Krok po kroku

1. 💾 Sklonuj repozytorium.
2. 🔑 Skonfiguruj dane dostępowe do bazy danych w `application.properties`.
3. 🌐 Ustaw zmienne środowiskowe `WEATHER_API_KEY` i `OPENAI_API_KEY` lub podaj je w lokalnym pliku konfiguracyjnym, który nie jest wersjonowany.
4. 📂 Zbuduj: `mvn clean install`
5. 🚀 Uruchom: `mvn spring-boot:run`
6. 🏠 Wejdź na: [http://localhost:8080](http://localhost:8080)

---

## Dane i testy regresji

Schemat bazy jest aktualizowany (`ddl-auto=update`); zamkniecie aplikacji nie usuwa
zamowien ani kont. To ustawienie dla projektu lokalnego, bez wersjonowanych migracji.
Role USER i ADMIN sa inicjalizowane, jesli nie istnieja. Konta i produkty demonstracyjne
sa domyslnie wylaczone. `DEMO_DATA_ENABLED=true` wlacza ich dodanie tylko do bazy bez
uzytkownikow. Konta demo maja publicznie znane hasla z kodu inicjalizatora.

Koszyk jest przechowywany w sesji przegladarki. Po wygasnieciu sesji lub restarcie
aplikacji nie jest odtwarzany z bazy. Zlozone zamowienia sa zapisywane w bazie wraz
z pozycjami, aktualnymi cenami produktow i kopia adresu dostawy. Po bledzie zapisu
zawartosc koszyka pozostaje dostepna. Puste zamowienia sa odrzucane.

Testy regresji bez Dockera (zapis zamowienia sprawdzany na testowej bazie H2):

```shell
mvn -Dtest=CheckoutPersistenceTest,BasketFailureTest,CheckoutControllerTest,BasketTest,JwtSecurityTest,RegistrationSecurityTest,RegistrationIdentityTest,DbInitTest test
```

## 📄 Licencja

Projekt udostępniany na licencji **MIT**. Możesz z niego korzystać komercyjnie i prywatnie.

---

Z miłości do gastronomii i technologii ❤️

**Big Piig Dev Team**
