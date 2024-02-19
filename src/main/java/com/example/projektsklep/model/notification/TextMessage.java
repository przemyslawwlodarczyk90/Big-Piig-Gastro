package com.example.projektsklep.model.notification;


import com.example.projektsklep.model.entities.order.Observable;
import com.example.projektsklep.model.entities.order.Order;

public class TextMessage implements Observer {

    private Order order;

    public TextMessage(Order order) {
        this.order = order;
    }

    @Override
    public void update(Observable observable) {
        if (observable instanceof Order) {
            Order order = (Order) observable;
            System.out.println("SMS - zamówienie numer: " + order.getId() + " zmieniło status na: " + order.getOrderStatus());
        }
    }

}