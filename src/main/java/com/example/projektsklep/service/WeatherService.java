package com.example.projektsklep.service;

import com.example.projektsklep.exception.InvalidCityException;
import com.example.projektsklep.model.repository.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.security.Principal;
import java.util.Optional;

@Service
public class WeatherService {

    // Wstrzykiwanie klucza API i URL do serwisu pogodowego z pliku properties.
    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.url}")
    private String apiUrl;


    private final UserRepository userRepository;


    public WeatherService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Metoda zwracająca dane pogodowe w zależności od podanego miasta lub, w przypadku braku miasta, dla domyślnej lokalizacji użytkownika.
    public Optional<String> getWeatherData(String city, Principal principal) {
        if (city != null && !city.isBlank()) {
            // Jeśli podano nazwę miasta, próbuje pobrać i zwrócić dane pogodowe dla tego miasta.
            return Optional.ofNullable(getWeatherForCity(city));
        } else if (principal != null) {
            // W przypadku braku miasta i dostępności principal, pobiera dane pogodowe dla domyślnej lokalizacji użytkownika.
            return Optional.ofNullable(getDefaultWeather(principal.getName()));
        }
        return Optional.empty(); // Zwraca pusty Optional, jeśli brakuje danych wejściowych.
    }

    // Pobiera dane pogodowe dla konkretnego miasta.
    public String getWeatherForCity(String city) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            // Budowanie URL zapytania o pogodę z wykorzystaniem nazwy miasta, klucza API i jednostek metrycznych.
            String url = apiUrl + "?q=" + city + "&appid=" + apiKey + "&units=metric";
            // Wykonanie zapytania i pobranie odpowiedzi jako łańcuch znaków.
            String response = restTemplate.getForObject(url, String.class);
            // Formatowanie i zwracanie sformatowanych danych pogodowych.
            return formatWeatherData(response);
        } catch (HttpClientErrorException e) {
            // Rzucenie wyjątku, jeśli miasto jest nieprawidłowe lub nie znaleziono dla niego danych pogodowych.
            throw new InvalidCityException("Weather information not available for the city: " + city);
        }
    }

    // Pobiera domyślne dane pogodowe na podstawie adresu e-mail użytkownika.
    public String getDefaultWeather(String userEmail) {
        return userRepository.findByEmail(userEmail)
                .flatMap(user -> Optional.ofNullable(user.getAddress()))
                .map(address -> getWeatherForCity(address.getCity()))
                .orElse("Nie można znaleźć danych o pogodzie dla Twojej lokalizacji.");
    }

    // Formatuje surowe dane JSON z odpowiedzi serwisu pogodowego na czytelny format tekstowy.
    private String formatWeatherData(String jsonData) {
        JSONObject jsonObject = new JSONObject(jsonData);
        // Ekstrakcja danych o pogodzie z obiektu JSON.
        String city = jsonObject.getString("name");
        JSONObject main = jsonObject.getJSONObject("main");
        double temp = main.getDouble("temp");
        int humidity = main.getInt("humidity");
        JSONObject weather = jsonObject.getJSONArray("weather").getJSONObject(0);
        String description = weather.getString("description");

        // Formatowanie i zwracanie danych pogodowych w formie łańcucha znaków.
        return String.format("City: %s\nTemperature: %.1f°C\nHumidity: %d%%\nDescription: %s",
                city, temp, humidity, description);
    }
}