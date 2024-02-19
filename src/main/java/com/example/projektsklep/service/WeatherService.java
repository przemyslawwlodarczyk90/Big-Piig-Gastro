package com.example.projektsklep.service;

import com.example.projektsklep.exception.InvalidCityException;
import com.example.projektsklep.model.entities.user.User;
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

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.url}")
    private String apiUrl;

    private final UserRepository userRepository;

    public WeatherService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<String> getWeatherData(String city, Principal principal) {
        if (city != null && !city.isBlank()) {
            return Optional.ofNullable(getWeatherForCity(city));
        } else if (principal != null) {
            return Optional.ofNullable(getDefaultWeather(principal.getName()));
        }
        return Optional.empty();
    }

    public String getWeatherForCity(String city) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = apiUrl + "?q=" + city + "&appid=" + apiKey + "&units=metric";
            String response = restTemplate.getForObject(url, String.class);
            return formatWeatherData(response);
        } catch (HttpClientErrorException e) {
            throw new InvalidCityException("Weather information not available for the city: " + city);
        }
    }

    public String getDefaultWeather(String userEmail) {
        return userRepository.findByEmail(userEmail)
                .flatMap(user -> Optional.ofNullable(user.getAddress()))
                .map(address -> getWeatherForCity(address.getCity()))
                .orElse("Nie można znaleźć danych o pogodzie dla Twojej lokalizacji.");
    }

    private String formatWeatherData(String jsonData) {
        JSONObject jsonObject = new JSONObject(jsonData);
        String city = jsonObject.getString("name");
        JSONObject main = jsonObject.getJSONObject("main");
        double temp = main.getDouble("temp");
        int humidity = main.getInt("humidity");
        JSONObject weather = jsonObject.getJSONArray("weather").getJSONObject(0);
        String description = weather.getString("description");

        return String.format("City: %s\nTemperature: %.1f°C\nHumidity: %d%%\nDescription: %s",
                city, temp, humidity, description);
    }
}