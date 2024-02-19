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
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderTest {

    @Mock
    private Observer mockObserver;

    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testNotifyObservers_ShouldNotifyRegisteredObservers() {
        // Given
        Order order = new Order();
        order.registerObserver(mockObserver);

        // When
        order.notifyObservers();

        // Then
        verify(mockObserver).update(order);
    }

    @Test
    public void testChangeOrderStatus_ShouldChangeStatusAndNotifyObservers() {
        // Given
        Order order = new Order();
        order.registerObserver(mockObserver);
        order.setOrderStatus(OrderStatus.PENDING);

        // When
        order.changeOrderStatus(OrderStatus.SHIPPED);

        // Then
        assertEquals(OrderStatus.SHIPPED, order.getOrderStatus());
        verify(mockObserver).update(order);
    }

    @Test
    public void testRegisterObserver_ShouldAddObserverToList() {
        // Given
        Order order = new Order();
        Observer mockObserver = Mockito.mock(Observer.class);

        // When
        order.registerObserver(mockObserver);

        // Then
        List<Observer> observers = order.getRegisteredObservers();
        assertTrue(observers.contains(mockObserver));
    }

    @Test
    public void testUnregisterObserver_ShouldRemoveObserverFromList() {
        // Given
        Order order = new Order();
        Observer mockObserver = Mockito.mock(Observer.class);
        order.registerObserver(mockObserver);

        // When
        order.unregisterObserver(mockObserver);

        // Then
        List<Observer> observers = order.getRegisteredObservers();
        assertFalse(observers.contains(mockObserver));
    }


    @Test
    public void testSetUser_GivenOrderAndUser_ShouldSetUser() {
        // Given
        User user = new User("user@example.com", "password", "avatar.png", "John", "Doe");
        Order order = new Order();

        // When
        order.setUser(user);

        // Then
        assertEquals(user, order.getUser());
    }

    @Test
    public void testGetUser_WithUserSet_ShouldReturnUser() {
        // Given
        User user = new User("user@example.com", "password", "avatar.png", "John", "Doe");
        Order order = new Order();
        order.setUser(user);

        // When
        User retrievedUser = order.getUser();

        // Then
        assertEquals(user, retrievedUser);
    }


}
