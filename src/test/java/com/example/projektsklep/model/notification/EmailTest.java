package com.example.projektsklep.model.notification;


import com.example.projektsklep.model.entities.order.Order;
import com.example.projektsklep.model.enums.OrderStatus;
import org.junit.Test;


import static org.junit.Assert.*;

public class EmailTest {


    @Test
    public void testConstructor() {

        Order order = new Order();
        order.setId(123L);

        Email email = new Email(order);

        assertNotNull(email);
    }

    @Test
    public void testUpdate_withOrder() {

        Order order = new Order();
        order.setId(123L);
        order.setOrderStatus(OrderStatus.DELIVERED); // Ensure status is set
        Email email = new Email(order);


        email.update(order);


        assertEquals("E-mail - zamówienie numer: 123 zmieniło status na: DELIVERED", email.getCapturedOutput());
    }

    @Test
    public void testUpdate_withNonOrderObservable() {

        Email email = new Email(new Order());
        Order nonOrder = new Order();

        email.update(nonOrder);
        assertEquals("", email.getCapturedOutput());
    }

    @Test
    public void testGetCapturedOutput() {
        Email email = new Email(new Order());

        String output = email.getCapturedOutput();

        assertEquals("", output);
    }
}