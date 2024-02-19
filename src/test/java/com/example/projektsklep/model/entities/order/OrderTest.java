import com.example.projektsklep.model.entities.order.Order;
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

/**
 * Testy jednostkowe dla klasy Order, sprawdzające zachowanie klasy w różnych scenariuszach.
 * Używa Mockito do mockowania zależności i weryfikacji interakcji.
 */
@ExtendWith(MockitoExtension.class)
class OrderTest {

    @Mock
    private Observer mockObserver; // Mock obserwatora wykorzystany w testach

    /**
     * Inicjalizuje mocki przed każdym testem.
     */
    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Testuje powiadomienie obserwatorów o zmianie w zamówieniu.
     */
    @Test
    public void testNotifyObservers_ShouldNotifyRegisteredObservers() {
        // Given
        Order order = new Order();
        order.registerObserver(mockObserver);

        // When
        order.notifyObservers();

        // Then
        verify(mockObserver).update(order); // Sprawdza, czy obserwator został powiadomiony
    }

    /**
     * Testuje zmianę statusu zamówienia i powiadomienie obserwatorów.
     */
    @Test
    public void testChangeOrderStatus_ShouldChangeStatusAndNotifyObservers() {
        // Given
        Order order = new Order();
        order.registerObserver(mockObserver);
        order.setOrderStatus(OrderStatus.PENDING);

        // When
        order.changeOrderStatus(OrderStatus.SHIPPED);

        // Then
        assertEquals(OrderStatus.SHIPPED, order.getOrderStatus()); // Sprawdza, czy status się zmienił
        verify(mockObserver).update(order); // Sprawdza, czy obserwator został powiadomiony o zmianie
    }

    /**
     * Testuje rejestrację obserwatora.
     */
    @Test
    public void testRegisterObserver_ShouldAddObserverToList() {
        // Given
        Order order = new Order();
        Observer mockObserver = Mockito.mock(Observer.class);

        // When
        order.registerObserver(mockObserver);

        // Then
        List<Observer> observers = order.getRegisteredObservers();
        assertTrue(observers.contains(mockObserver)); // Sprawdza, czy obserwator został dodany
    }

    /**
     * Testuje wyrejestrowanie obserwatora.
     */
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
        assertFalse(observers.contains(mockObserver)); // Sprawdza, czy obserwator został usunięty
    }

    /**
     * Testuje ustawienie użytkownika dla zamówienia.
     */
    @Test
    public void testSetUser_GivenOrderAndUser_ShouldSetUser() {
        // Given
        User user = new User("user@example.com", "password", "avatar.png", "John", "Doe");
        Order order = new Order();

        // When
        order.setUser(user);

        // Then
        assertEquals(user, order.getUser()); // Sprawdza, czy użytkownik został prawidłowo przypisany do zamówienia
    }

    /**
     * Testuje pobieranie użytkownika z zamówienia.
     */
    @Test
    public void testGetUser_WithUserSet_ShouldReturnUser() {
        // Given
        User user = new User("user@example.com", "password", "avatar.png", "John", "Doe");
        Order order = new Order();
        order.setUser(user);

        // When
        User retrievedUser = order.getUser();

        // Then
        assertEquals(user, retrievedUser); // Sprawdza, czy zwrócony użytkownik jest taki sam jak ustawiony
    }
}