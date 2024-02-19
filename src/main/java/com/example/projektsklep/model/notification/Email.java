package com.example.projektsklep.model.notification;


import com.example.projektsklep.model.entities.order.Observable;
import com.example.projektsklep.model.entities.order.Order;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


public class Email implements Observer {

    private Order order;
    private ByteArrayOutputStream capturedOutput;

    public Email(Order order) {
        this.order = order;
        this.capturedOutput = new ByteArrayOutputStream();
    }

    @Override
    public void update(Observable observable) {
        if (observable instanceof Order) {
            Order order = (Order) observable;
            PrintStream oldStream = System.out;
            System.setOut(new PrintStream(capturedOutput));
            System.out.println("E-mail - zamówienie numer: " + order.getId() + " zmieniło status na: " + order.getOrderStatus());
            System.setOut(oldStream);
        }
    }

    public String getCapturedOutput() {
        return capturedOutput.toString();
    }
}