# Uruchomienie lokalne

Projekt uzywa JDK 17. Zainstalowana globalnie Java 25 moze pozostac; ten projekt
korzysta z oddzielnego JDK. Stary Spring Boot 3.2 i Lombok nie sa konfiguracja
przeznaczona dla JDK 25.

## Windows

```powershell
.\scripts\dev.ps1
```

Skrypt znajduje JDK 17 z `JAVA17_HOME`, `JAVA_HOME` albo katalogu `%USERPROFILE%/.jdks`.
Mozna tez wskazac sciezke jawnie:

```powershell
.\scripts\dev.ps1 -JavaHome C:\Users\twoj-login\.jdks\ms-17.0.17
.\scripts\dev.ps1 -Verify
.\scripts\dev.ps1 -Port 8091
```

Maven Wrapper pobiera ustalona wersje Mavena. Ustawienie Javy dotyczy tylko
wywolania skryptu i nie zmienia globalnej konfiguracji komputera.

Profil `local` uruchamia aplikacje na `http://localhost:8080` z plikowa baza H2 w
`.local-data/`. Dane przezywaja restart; katalog nie trafia do Gita. Docker ani
PostgreSQL nie sa potrzebne w tym profilu. Dane demonstracyjne powstaja tylko
w bazie bez uzytkownikow: `user@example.com` / `user` i `admin@example.com` / `admin`.
Te konta sa przeznaczone wylacznie do demonstracji.

Skrypt generuje tymczasowy sekret JWT, jesli `JWT_SECRET` nie jest ustawiony.
Po ponownym uruchomieniu z nowym sekretem trzeba sie ponownie zalogowac.
Pogoda i asystent AI wymagaja prawdziwych `WEATHER_API_KEY` i `OPENAI_API_KEY`;
lokalne wartosci zastepcze pozwalaja uruchomic sklep, ale nie realizuja tych integracji.

## IntelliJ: blad JVM target 5

1. Otworz projekt jako projekt Maven z `pom.xml` i wykonaj Reload All Maven Projects.
2. Project Structure: Project SDK i Module SDK ustaw na JDK 17, language level na 17.
3. Java Compiler: Project bytecode version i target modulu `Projekt-Sklep` ustaw na 17.
4. Maven Runner i Maven Importer: wybierz Project JDK 17. Wlacz annotation processing.

W `pom.xml` poziomy `release`, `source` i `target` sa jawnie ustawione na 17.
Lokalne pliki `.idea` nie sa wersjonowane; import Mavena jest zrodlem konfiguracji.

## PostgreSQL i przyszle wdrozenie

Domyslny profil pozostaje przy PostgreSQL. Uruchom baze (`docker compose up -d`)
i ustaw `JWT_SECRET`, klucze integracji oraz dane polaczenia przez zmienne Springa.
Nie uzywaj profilu `local` jako konfiguracji AWS. Symulacje magazynu wlacza
`DEMO_INVENTORY_ENABLED=true`; ciasteczko JWT domyslnie wymaga HTTPS.
