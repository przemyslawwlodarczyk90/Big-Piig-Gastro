package com.example.projektsklep.controller;

import com.example.projektsklep.exception.ErrorHandlingException;
import com.example.projektsklep.exception.ProductNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.LoggerFactory;


@ControllerAdvice
@Controller
public class ErrorController {
    private static final Logger logger = LoggerFactory.getLogger(ErrorController.class);


    @Operation(summary = "Obsługuje niestandardowe błędy w aplikacji")
    @RequestMapping("/customError")
    public String handleError(HttpServletRequest request, Model model) {
        try {
            Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

            if (status != null) {
                int statusCode = Integer.parseInt(status.toString());
                String message = HttpStatus.valueOf(statusCode).getReasonPhrase();

                throw new ErrorHandlingException(statusCode, message);
            }

            model.addAttribute("error", "Nieznany błąd");
            return "error";
        } catch (ErrorHandlingException e) {
            model.addAttribute("status", e.getStatusCode());
            model.addAttribute("error", e.getMessage());
            return "error";
        } catch (Exception e) {
            logger.error("Błąd podczas obsługi błędu", e);
            model.addAttribute("error", "Wystąpił nieoczekiwany błąd");
            return "error";
        }
    }

    @Operation(summary = "Obsługuje wyjątek 'ProductNotFoundException'")
    @ExceptionHandler(ProductNotFoundException.class)
    public String handleProductNotFound(Model model) {
        model.addAttribute("error", "Produkt nie znaleziony");
        return "product_not_found";
    }


}