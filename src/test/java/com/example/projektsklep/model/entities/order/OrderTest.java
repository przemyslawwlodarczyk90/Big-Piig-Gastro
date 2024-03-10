package com.example.projektsklep.model.entities.order;

import com.example.projektsklep.model.entities.user.User;
import com.example.projektsklep.model.enums.OrderStatus;
import com.example.projektsklep.model.notification.Observer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class OrderTest {

    @Mock
    private Observer mockObserver;

    @BeforeEach
     void initMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
     void testNotifyObservers_ShouldNotifyRegisteredObservers() {

        Order order = new Order();
        order.registerObserver(mockObserver);

        order.notifyObservers();

        verify(mockObserver).update(order);
    }


    @Test
     void testChangeOrderStatus_ShouldChangeStatusAndNotifyObservers() {

        Order order = new Order();
        order.registerObserver(mockObserver);
        order.setOrderStatus(OrderStatus.PENDING);

        order.changeOrderStatus(OrderStatus.SHIPPED);

        assertEquals(OrderStatus.SHIPPED, order.getOrderStatus());
        verify(mockObserver).update(order);
    }

    @Test
     void testRegisterObserver_ShouldAddObserverToList() {

        Order order = new Order();
        Observer mockObserver = Mockito.mock(Observer.class);

        order.registerObserver(mockObserver);

        List<Observer> observers = order.getRegisteredObservers();
        assertTrue(observers.contains(mockObserver));
    }


    @Test
     void testUnregisterObserver_ShouldRemoveObserverFromList() {

        Order order = new Order();
        Observer mockObserver = Mockito.mock(Observer.class);
        order.registerObserver(mockObserver);

        order.unregisterObserver(mockObserver);

        List<Observer> observers = order.getRegisteredObservers();
        assertFalse(observers.contains(mockObserver));
    }


    @Test
     void testSetUser_GivenOrderAndUser_ShouldSetUser() {

        User user = new User("user@example.com", "password", "avatar.png", "John", "Doe");
        Order order = new Order();

        order.setUser(user);

        assertEquals(user, order.getUser());
    }


    @Test
     void testGetUser_WithUserSet_ShouldReturnUser() {

        User user = new User("user@example.com", "password", "avatar.png", "John", "Doe");
        Order order = new Order();
        order.setUser(user);

        User retrievedUser = order.getUser();

        assertEquals(user, retrievedUser);
    }
}