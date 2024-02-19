package com.example.projektsklep.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public String handleProductNotFound(Model model) {
        // ... obsłuż błąd
        return null;
    }
    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFound(Model model) {
        model.addAttribute("error", "Użytkownik nie znaleziony");
        return "user_not_found";
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public String handleOrderNotFound(Model model) {
        model.addAttribute("error", "Zamówienie nie znalezione");
        return "order_not_found";
    }

    @ExceptionHandler(AddressUpdateException.class)
    public String handleAddressUpdateError(Model model) {
        model.addAttribute("error", "Wystąpił błąd podczas aktualizacji adresu");
        return "update_profile_error";
    }
    @ExceptionHandler(UserAlreadyExistsException.class)
    public String handleUserAlreadyExists(Model model) {
        model.addAttribute("error", "Użytkownik o tym adresie e-mail już istnieje");
        return "user_register";
    }

    @ExceptionHandler(InvalidUserDataException.class)
    public String handleInvalidUserData(Model model) {
        model.addAttribute("error", "Błędne dane użytkownika");
        return "user_edit";
    }

    @ExceptionHandler(DataAccessException.class)
    public String handleDataAccessException(Model model) {
        model.addAttribute("dataAccessError", "Wystąpił błąd podczas dostępu do danych");
        return "weather";
    }
    @ExceptionHandler(WeatherServiceException.class)
    public String handleWeatherServiceError(Model model) {
        model.addAttribute("error", "Wystąpił błąd podczas pobierania danych pogodowych");
        return "weather_error";
    }

    @ExceptionHandler(InvalidCityException.class)
    public String handleInvalidCityError(Model model) {
        model.addAttribute("error", "Podane miasto jest nieprawidłowe");
        return "invalid_city_error";
    }

    @ExceptionHandler(WeatherServiceUnavailableException.class)
    public String handleWeatherServiceUnavailableError(Model model) {
        model.addAttribute("error", "Serwis pogodowy jest niedostępny. Spróbuj ponownie później.");
        return "weather_error";
    }


}