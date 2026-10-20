package com.example.projektsklep.service;

import com.example.projektsklep.model.dto.*;
import com.example.projektsklep.model.entities.product.Product;
import com.example.projektsklep.model.repository.ProductRepository;
import com.example.projektsklep.utils.Basket;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@SessionScope
public class BasketService {
    private final ProductRepository products;
    private final OrderCheckoutService checkout;
    private final Basket basket = new Basket();

    public BasketService(ProductRepository products, OrderCheckoutService checkout) {
        this.products = products;
        this.checkout = checkout;
    }

    public synchronized Basket getCurrentBasket() { return basket; }

    public synchronized void addProductToBasket(Long productId, int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("Ilosc musi byc dodatnia");
        Product product = products.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono produktu: " + productId));
        basket.addProduct(product, quantity);
    }

    public synchronized void clear() { basket.clear(); }

    public synchronized void takeOneItemFromQuantity(Long productId) {
        basket.getLineOfOrders().stream().filter(line -> line.getProduct().getId().equals(productId))
                .findFirst().ifPresent(line -> updateProductQuantity(productId, line.getQuantity() - 1));
    }

    public synchronized void updateProductQuantity(Long productId, int quantity) {
        if (quantity < 0) throw new IllegalArgumentException("Ilosc nie moze byc ujemna");
        if (quantity == 0) {
            basket.getLineOfOrders().removeIf(line -> line.getProduct().getId().equals(productId));
        } else {
            basket.getLineOfOrders().stream().filter(line -> line.getProduct().getId().equals(productId))
                    .findFirst().ifPresent(line -> line.setQuantity(quantity));
        }
    }

    public synchronized OrderDTO createOrderDTOFromBasket(Long userId) {
        List<LineOfOrderDTO> lines = basket.getLineOfOrders().stream().map(line -> {
            Product product = products.findById(line.getProduct().getId()).orElseThrow(
                    () -> new IllegalArgumentException("Produkt nie jest juz dostepny"));
            return new LineOfOrderDTO(null, product.getId(), line.getQuantity(), product.getPrice());
        }).toList();
        BigDecimal total = lines.stream().map(line -> line.unitPrice().multiply(BigDecimal.valueOf(line.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new OrderDTO(null, userId, "NEW_ORDER", LocalDate.now(), null, total, lines,
                new AddressDTO(null, "", "", "", ""));
    }

    public synchronized OrderDTO prepareOrderForCheckout(Long userId) { return createOrderDTOFromBasket(userId); }

    public synchronized void placeOrder(OrderDTO order) {
        // Clear only after the transactional checkout has successfully committed.
        checkout.placeOrder(order.userId(), basket.getLineOfOrders(), order.shippingAddress());
        clear();
    }

    public synchronized String finalizeOrderAndRedirect(Long userId, AddressDTO address, boolean differentAddress) {
        checkout.placeOrder(userId, basket.getLineOfOrders(), differentAddress ? address : null);
        clear();
        return "redirect:/account/orderSuccess";
    }
}
