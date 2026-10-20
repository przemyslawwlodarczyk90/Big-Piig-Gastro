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
import java.util.Comparator;
import java.time.Clock;

@Service
public class OrderCheckoutService {
    private final OrderRepository orders;
    private final UserRepository users;
    private final ProductRepository products;
    private final AddressRepository addresses;
    private final Clock clock;

    public OrderCheckoutService(OrderRepository orders, UserRepository users,
                                ProductRepository products, AddressRepository addresses, Clock clock) {
        this.orders = orders;
        this.users = users;
        this.products = products;
        this.addresses = addresses;
        this.clock = clock;
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
        for (var item : basket.stream().sorted(Comparator.comparing(line -> line.getProduct().getId())).toList()) {
            if (item.getQuantity() <= 0) throw new IllegalArgumentException("Ilosc musi byc dodatnia");
            var product = products.findForUpdate(item.getProduct().getId()).orElseThrow(
                    () -> new IllegalArgumentException("Produkt nie jest juz dostepny"));
            if (product.availableStock(clock.instant()) < item.getQuantity()) {
                throw new IllegalArgumentException("Za malo sztuk produktu: " + product.getTitle());
            }
            product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
            var line = new LineOfOrder(product, item.getQuantity());
            line.setOrder(order);
            order.getLineOfOrders().add(line);
        }
        order.calculateTotalPrice();
        orders.saveAndFlush(order);
    }

    private boolean blank(String value) { return value == null || value.isBlank(); }
}
