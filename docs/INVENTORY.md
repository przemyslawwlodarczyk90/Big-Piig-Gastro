# Magazyn demonstracyjny

Sklep ma pokazywac dzialanie zakupow bez recznego uzupelniania produktow po kazdej
wizycie. Generator jest symulacja dostawcy, nie integracja z prawdziwym magazynem.

## Zasady

- Produkt przechowuje liczbe sztuk, termin konca chwilowej niedostepnosci i wersje rekordu.
- Dodanie do koszyka sprawdza stan, ale go nie rezerwuje. Porzucony koszyk nie blokuje towaru.
- Finalizacja ponownie sprawdza dostepnosc i odejmuje sztuki w tej samej transakcji
  co zapis zamowienia. Niedobor jednej pozycji cofa cale zamowienie, adres i pobrane sztuki.
- Blokady `PESSIMISTIC_WRITE` chronia ostatnia sztuke przed dwoma zakupami naraz.
  Pozycje sa blokowane w kolejnosci ID, wspolnej z generatorem.
- Koszyk pozostaje po bledzie. Cena i stan przeslane przez formularz nie sa zrodlem prawdy.

## Losowe dostawy i opoznienia

Przy wlaczonej symulacji pierwsze sprawdzenie nastepuje po 10 sekundach, potem co
60 sekund od zakonczenia poprzedniego przebiegu. Stan ponizej 5 sztuk dostaje losowa
dostawe 10–30 sztuk. Wybor jest ograniczony: generator nie obniza zapasu i nie
dodaje towaru bez konca, gdy nikt nie kupuje.

Przy co najmniej dwoch produktach jeden z nich ma losowe opoznienie dostawcy na
2–5 minut. Klient widzi dla niego 0 dostepnych sztuk, mimo ze fizyczny zapas zostaje
w bazie. Pozwala to pokazac brak dostepnosci bez kasowania towaru. Kolejne przebiegi
nie przedluzaja aktywnego opoznienia. Po jego koncu produkt wraca, a generator
wybiera kolejny z produktow, ktore nie mialy jeszcze opoznienia w danym cyklu.
Przy jednym produkcie opoznienia sa wylaczone, zeby sklep pozostal uzyteczny.

Generator i zakup blokuja te same rekordy w bazie. Przy kilku instancjach aplikacji
przebiegi sa serializowane; aktywne opoznienie i uzupelniony stan zapobiegaja
ponownemu dodaniu tej samej dostawy. Czas symulacji jest zapisany w bazie, wiec
restart aplikacji nie usuwa aktywnego opoznienia.

Rozwiazanie jest przeznaczone dla malego katalogu demo: generator blokuje caly
katalog na czas krotkiej transakcji. Dla duzego sklepu potrzebne bylyby partiowanie,
historia ruchow magazynowych i prawdziwa obsluga rezerwacji.

## Wlaczenie

```text
DEMO_INVENTORY_ENABLED=true
DEMO_INVENTORY_INTERVAL_MS=60000
```

Domyslnie symulacja jest wylaczona. Profil `local` wlacza ja razem z danymi demo.
`DEMO_DATA_ENABLED` odpowiada za poczatkowe konta i produkty; to osobna funkcja od
cyklicznego uzupelniania stanow. Bez symulacji nowy produkt ma 0 sztuk.

## Testy

Testy jednostkowe uzywaja ustalonego zegara i kontrolowanego generatora losowego.
Sprawdzaja granice progu, wielkosc dostawy, czas niedostepnosci, rotacje i pusty katalog.
Testy transakcyjne na H2 sprawdzaja dwa zakupy ostatniej sztuki, wycofanie wszystkich
zmian po niedoborze oraz blokade zakupu podczas opoznienia dostawcy.

Te same scenariusze mozna uruchomic na PostgreSQL z Testcontainers:

```shell
./mvnw verify -Ppostgres-it
```

Ten profil wymaga dzialajacego Dockera. Zwykle `verify` uruchamia caly zestaw bez
zewnetrznej bazy ani kluczy API i kontroluje pokrycie linii JaCoCo na poziomie 70%.
Raport: `target/site/jacoco/index.html`. Kod wygenerowany przez Lomboka jest oznaczony
`@lombok.Generated`; recznie napisane klasy nie sa wykluczane.
