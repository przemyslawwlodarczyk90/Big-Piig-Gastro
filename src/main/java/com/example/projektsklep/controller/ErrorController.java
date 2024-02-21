package com.example.projektsklep.controller;

import com.example.projektsklep.exception.ErrorHandlingException;
import com.example.projektsklep.exception.OrderNotFoundException;
import com.example.projektsklep.exception.ProductNotFoundException;
import com.example.projektsklep.model.dto.OrderDTO;
import com.example.projektsklep.service.OrderService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@ControllerAdvice
@Controller
public class ErrorController {
    private static final Logger logger = LoggerFactory.getLogger(ErrorController.class);
    private final OrderService orderService;

    public ErrorController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Obsługuje niestandardowe błędy w aplikacji.
     * Metoda przechwytuje błędy i wyświetla odpowiednią stronę błędu.
     *
     * @param request Zawiera żądanie, które spowodowało błąd.
     * @param model Model do przekazania danych do widoku.
     * @return Nazwa widoku błędu do wyświetlenia.
     */
    @RequestMapping("/customError")
    public String handleError(HttpServletRequest request, Model model) {
        try {
            // Pobierz kod statusu błędu z żądania.
            Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

            // Jeśli status jest dostępny, rzuć wyjątek do dalszej obsługi.
            if (status != null) {
                int statusCode = Integer.parseInt(status.toString());
                String message = HttpStatus.valueOf(statusCode).getReasonPhrase();

                throw new ErrorHandlingException(statusCode, message);
            }

            // W przypadku braku kodu statusu, zwróć ogólną stronę błędu.
            model.addAttribute("error", "Nieznany błąd");
            return "error";
        } catch (ErrorHandlingException e) {
            // Obsłuż niestandardowy wyjątek i zaktualizuj model o szczegóły błędu.
            model.addAttribute("status", e.getStatusCode());
            model.addAttribute("error", e.getMessage());
            return "error";
        } catch (Exception e) {
            // Loguj nieoczekiwane wyjątki i zwróć ogólną stronę błędu.
            logger.error("Błąd podczas obsługi błędu", e);
            model.addAttribute("error", "Wystąpił nieoczekiwany błąd");
            return "error";
        }
    }

    /**
     * Obsługuje wyjątki typu ProductNotFoundException.
     * Ustawia model i zwraca nazwę widoku dla błędu "Produkt nie znaleziony".
     *
     * @param model Model do przekazania danych do widoku.
     * @return Nazwa widoku błędu produktu.
     */
    @ExceptionHandler(ProductNotFoundException.class)
    public String handleProductNotFound(Model model) {
        model.addAttribute("error", "Produkt nie znaleziony");
        return "product_not_found";
    }


}