package com.example.projektsklep.exception;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(String orderId) {
        super("Zamówienie o ID " + orderId + " nie zostało znalezione");
    }


}