package com.example.projektsklep.controller;

import com.example.projektsklep.exception.ErrorHandlingException;
import com.example.projektsklep.exception.ProductNotFoundException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.slf4j.LoggerFactory;

@Controller
public class ErrorController {

    private static final Logger logger = LoggerFactory.getLogger(ErrorController.class);

    @ResponseStatus
    @RequestMapping("/customError")

    public String handleError(HttpServletRequest request, Model model) {

        try {
            Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

            if (status != null) {
                int statusCode = Integer.parseInt(status.toString());
                String message = HttpStatus.valueOf(statusCode).getReasonPhrase();

                throw new ErrorHandlingException(statusCode, message);
            }


        } catch (ErrorHandlingException e) {
            model.addAttribute("status", e.getStatusCode());
            model.addAttribute("error", e.getMessage());

            return "error";
        } catch (Exception e) {
            ErrorController.logger.error("Błąd podczas obsługi błędu", e);


            throw e;
        }
        return "error";
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public String handleProductNotFound(Model model) {
        model.addAttribute("error", "Produkt nie znaleziony");
        return "product_not_found"; // Nazwa widoku dla błędu
    }
}

