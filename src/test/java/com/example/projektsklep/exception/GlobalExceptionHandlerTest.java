package com.example.projektsklep.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
 class GlobalExceptionHandlerTest {

    @Mock
    private Model model;

    @Test
     void testHandleProductNotFound() {
        // Simulate throwing ProductNotFoundException
        Exception exception = new ProductNotFoundException("Product not found message");

        String viewName = new GlobalExceptionHandler().handleProductNotFound(exception);

        assertNull(viewName); // Expecting null as specified in the handler

        // Verify the exception message is set correctly
        assertEquals("Product not found message", exception.getMessage());
    }

    @Test
     void testHandleUserNotFound() {

        String viewName = new GlobalExceptionHandler().handleUserNotFound(model);

        assertEquals("user_not_found", viewName);
        verify(model).addAttribute("error", "Użytkownik nie znaleziony");
    }



    @Test
     void testHandleOrderNotFound() {

        String viewName = new GlobalExceptionHandler().handleOrderNotFound(model);

        assertEquals("order_not_found", viewName);
        verify(model).addAttribute("error", "Zamówienie nie znalezione");
    }

    @Test
     void testHandleAddressUpdateError() {
        String expectedMessage = "Wystąpił błąd podczas aktualizacji adresu";

        String viewName = new GlobalExceptionHandler().handleAddressUpdateError(model);

        assertEquals("update_profile_error", viewName);
        verify(model).addAttribute("error", expectedMessage);
    }

    @Test
     void testHandleUserAlreadyExists() {
        String expectedMessage = "Użytkownik o tym adresie e-mail już istnieje";

        String viewName = new GlobalExceptionHandler().handleUserAlreadyExists(model);

        assertEquals("user_register", viewName);
        verify(model).addAttribute("error", expectedMessage);
    }


    @Test
     void testHandleInvalidUserData() {
        String expectedMessage = "Błędne dane użytkownika";


        String viewName = new GlobalExceptionHandler().handleInvalidUserData(model);

        assertEquals("user_edit", viewName);
        verify(model).addAttribute("error", expectedMessage);
    }

    @Test
     void testHandleDataAccessException() {
        String expectedMessage = "Wystąpił błąd podczas dostępu do danych";


        String viewName = new GlobalExceptionHandler().handleDataAccessException(model);

        assertEquals("weather", viewName);
        verify(model).addAttribute("dataAccessError", expectedMessage);
    }

    @Test
     void testHandleWeatherServiceException() {
        String expectedMessage = "Wystąpił błąd podczas pobierania danych pogodowych";


        String viewName = new GlobalExceptionHandler().handleWeatherServiceError(model);

        assertEquals("weather_error", viewName);
        verify(model).addAttribute("error", expectedMessage);
    }

    @Test
     void testHandleInvalidCityError() {
        String expectedMessage = "Podane miasto jest nieprawidłowe";

        String viewName = new GlobalExceptionHandler().handleInvalidCityError(model);

        assertEquals("invalid_city_error", viewName);
        verify(model).addAttribute("error", expectedMessage);
    }
}