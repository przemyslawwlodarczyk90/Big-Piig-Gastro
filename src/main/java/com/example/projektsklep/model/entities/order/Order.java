package com.example.projektsklep.model.entities.order;

import com.example.projektsklep.model.entities.adress.Address;
import com.example.projektsklep.model.entities.product.Product;
import com.example.projektsklep.model.entities.user.User;
import com.example.projektsklep.model.enums.OrderStatus;
import com.example.projektsklep.model.notification.Observer;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa reprezentująca zamówienie w systemie sklepu.
 */
@Entity
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order implements Observable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Unikalny identyfikator zamówienia

    @ManyToOne
    @JoinColumn(name = "account_holder_id")
    private User accountHolder; // Właściciel zamówienia, użytkownik który złożył zamówienie

    @ManyToOne
    @JoinColumn(name = "shipping_address_id")
    private Address shippingAddress; // Adres wysyłki zamówienia

    @Enumerated(EnumType.ORDINAL)
    private OrderStatus orderStatus; // Status zamówienia

    @Column(name = "date_created")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateCreated; // Data utworzenia zamówienia

    @Column(name = "sent_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate sentAt; // Data wysyłki zamówienia

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<LineOfOrder> lineOfOrders = new ArrayList<>(); // Lista pozycji zamówienia

    private BigDecimal totalPrice; // Całkowita wartość zamówienia

    @Transient
    private List<Observer> registeredObservers = new ArrayList<>(); // Lista obserwatorów zamówienia

    /**
     * Oblicza całkowitą wartość zamówienia na podstawie sumy cen wszystkich pozycji zamówienia.
     */
    public void calculateTotalPrice() {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (LineOfOrder lineOfOrder : lineOfOrders) {
            totalPrice = totalPrice.add(lineOfOrder.getUnitPrice().multiply(new BigDecimal(lineOfOrder.getQuantity())));
        }
        this.totalPrice = totalPrice;
    }

    /**
     * Rejestruje obserwatora zamówienia.
     * @param observer Obserwator do zarejestrowania
     */
    @Override
    public void registerObserver(Observer observer) {
        registeredObservers.add(observer);
    }

    /**
     * Wyrejestrowuje obserwatora zamówienia.
     * @param observer Obserwator do wyrejestrowania
     */
    @Override
    public void unregisterObserver(Observer observer) {
        registeredObservers.remove(observer);
    }

    /**
     * Powiadamia wszystkich zarejestrowanych obserwatorów o zmianie w zamówieniu.
     */
    @Override
    public void notifyObservers() {
        for (Observer observer : registeredObservers) {
            observer.update(this);
        }
    }

    /**
     * Zmienia status zamówienia i powiadamia o tym fakcie obserwatorów.
     * @param orderStatus Nowy status zamówienia
     */



    public void changeOrderStatus(OrderStatus orderStatus) {
        setOrderStatus(orderStatus);
        notifyObservers();
    }


    // METODY DO TESTÓW

    /**
     * Ustawia użytkownika jako właściciela zamówienia.
     * @param user Użytkownik będący właścicielem zamówienia.
     */
    public void setUser(User user) {
        if (user != null) {
            this.accountHolder = user;
        }
    }

    /**
     * Zwraca użytkownika będącego właścicielem zamówienia.
     * @return Użytkownik będący właścicielem zamówienia.
     */
    public User getUser() {
        if (this.accountHolder != null) {
            return this.accountHolder;
        } else {
            return null;
        }
    }

    /**
     * Ustawia listę pozycji w zamówieniu.
     * @param lineOfOrders Lista pozycji zamówienia.
     */
    public void setListOfOrders(List<LineOfOrder> lineOfOrders) {
        this.lineOfOrders = lineOfOrders;
    }

    /**
     * Zwraca adres wysyłki dla zamówienia.
     * @return Adres wysyłki zamówienia.
     */
    public Address getAddress() {
        if (this.shippingAddress != null) {
            return this.shippingAddress;
        } else {
            return null;
        }
    }

    /**
     * Zwraca listę produktów związanych z zamówieniem.
     * @return Lista produktów zamówienia.
     */
    public List<Product> getProducts() {
        if (this.lineOfOrders != null) {
            List<Product> products = new ArrayList<>();
            for (LineOfOrder lineOfOrder : this.lineOfOrders) {
                products.add(lineOfOrder.getProduct());
            }
            return products;
        } else {
            return null;
        }
    }

}