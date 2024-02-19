package com.example.projektsklep.exception;

public class OrderRetrievalException extends RuntimeException {

    public OrderRetrievalException(String message) {
        super("Błąd podczas pobierania zamówień: " + message);
    }
}