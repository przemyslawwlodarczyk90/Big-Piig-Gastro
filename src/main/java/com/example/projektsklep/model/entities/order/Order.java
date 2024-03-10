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
import java.util.Collections;
import java.util.List;


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
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_holder_id")
    private User accountHolder;

    @ManyToOne
    @JoinColumn(name = "shipping_address_id")
    private Address shippingAddress;

    @Enumerated(EnumType.ORDINAL)
    private OrderStatus orderStatus;
    @Column(name = "date_created")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateCreated;

    @Column(name = "sent_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate sentAt;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<LineOfOrder> lineOfOrders = new ArrayList<>();

    private BigDecimal totalPrice;
    @Transient
    private List<Observer> registeredObservers = new ArrayList<>();


    public void calculateTotalPrice() {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (LineOfOrder lineOfOrder : lineOfOrders) {
            totalPrice = totalPrice.add(lineOfOrder.getUnitPrice().multiply(new BigDecimal(lineOfOrder.getQuantity())));
        }
        this.totalPrice = totalPrice;
    }


    @Override
    public void registerObserver(Observer observer) {
        registeredObservers.add(observer);
    }


    @Override
    public void unregisterObserver(Observer observer) {
        registeredObservers.remove(observer);
    }


    @Override
    public void notifyObservers() {
        for (Observer observer : registeredObservers) {
            observer.update(this);
        }
    }


    public void changeOrderStatus(OrderStatus orderStatus) {
        setOrderStatus(orderStatus);
        notifyObservers();
    }


    public void setUser(User user) {
        if (user != null) {
            this.accountHolder = user;
        }
    }

    public User getUser() {
        if (this.accountHolder != null) {
            return this.accountHolder;
        } else {
            return null;
        }
    }


    public void setListOfOrders(List<LineOfOrder> lineOfOrders) {
        this.lineOfOrders = lineOfOrders;
    }


    public Address getAddress() {
        if (this.shippingAddress != null) {
            return this.shippingAddress;
        } else {
            return null;
        }
    }


    public List<Product> getProducts() {
        if (this.lineOfOrders != null) {
            List<Product> products = new ArrayList<>();
            for (LineOfOrder lineOfOrder : this.lineOfOrders) {
                products.add(lineOfOrder.getProduct());
            }
            return products;
        } else {
            return Collections.emptyList();
        }
    }

}