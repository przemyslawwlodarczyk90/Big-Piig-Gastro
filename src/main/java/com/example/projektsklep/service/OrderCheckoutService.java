package com.example.projektsklep.service;

import com.example.projektsklep.model.dto.AddressDTO;
import com.example.projektsklep.model.entities.adress.Address;
import com.example.projektsklep.model.entities.order.LineOfOrder;
import com.example.projektsklep.model.entities.order.Order;
import com.example.projektsklep.model.enums.OrderStatus;
import com.example.projektsklep.model.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
public class OrderCheckoutService {
    private final OrderRepository orders;
    private final UserRepository users;
    private final ProductRepository products;
    private final AddressRepository addresses;

    public OrderCheckoutService(OrderRepository orders, UserRepository users,
                                ProductRepository products, AddressRepository addresses) {
        this.orders = orders;
        this.users = users;
        this.products = products;
        this.addresses = addresses;
    }

    @Transactional
    public void placeOrder(Long userId, List<LineOfOrder> basket, AddressDTO shipping) {
        if (basket.isEmpty()) throw new IllegalArgumentException("Koszyk jest pusty");
        var user = users.findById(userId).orElseThrow(() -> new IllegalArgumentException("Nie znaleziono uzytkownika"));
        var order = new Order();
        order.setAccountHolder(user);
        order.setOrderStatus(OrderStatus.NEW_ORDER);
        order.setDateCreated(LocalDate.now());
        AddressDTO delivery = shipping;
        if (delivery == null && user.getAddress() != null) {
            var address = user.getAddress();
            delivery = new AddressDTO(null, address.getStreet(), address.getCity(), address.getPostalCode(), address.getCountry());
        }
        if (delivery == null || blank(delivery.street()) || blank(delivery.city())
                || blank(delivery.postalCode()) || blank(delivery.country())) {
            throw new IllegalArgumentException("Podaj kompletny adres dostawy");
        }
        // Snapshot the address so later profile edits do not alter historical orders.
        order.setShippingAddress(addresses.save(new Address(null, delivery.street(), delivery.city(),
                delivery.postalCode(), delivery.country())));
        for (var item : basket) {
            if (item.getQuantity() <= 0) throw new IllegalArgumentException("Ilosc musi byc dodatnia");
            var product = products.findById(item.getProduct().getId()).orElseThrow(
                    () -> new IllegalArgumentException("Produkt nie jest juz dostepny"));
            var line = new LineOfOrder(product, item.getQuantity());
            line.setOrder(order);
            order.getLineOfOrders().add(line);
        }
        order.calculateTotalPrice();
        orders.saveAndFlush(order);
    }

    private boolean blank(String value) { return value == null || value.isBlank(); }
}
