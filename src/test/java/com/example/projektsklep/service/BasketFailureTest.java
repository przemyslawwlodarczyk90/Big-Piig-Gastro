package com.example.projektsklep.service;

import com.example.projektsklep.model.entities.product.Product;
import com.example.projektsklep.model.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BasketFailureTest {
    @Test
    void preservesBasketWhenSavingFails() {
        var products = mock(ProductRepository.class);
        var checkout = mock(OrderCheckoutService.class);
        var product = new Product("Oven", BigDecimal.TEN);
        product.setId(1L);
        when(products.findById(1L)).thenReturn(Optional.of(product));
        var basket = new BasketService(products, checkout);
        basket.addProductToBasket(1L, 2);
        doThrow(new org.springframework.dao.DataIntegrityViolationException("failed"))
                .when(checkout).placeOrder(eq(1L), anyList(), isNull());
        assertThrows(org.springframework.dao.DataIntegrityViolationException.class,
                () -> basket.finalizeOrderAndRedirect(1L, null, false));
        assertEquals(2, basket.getCurrentBasket().getLineOfOrders().get(0).getQuantity());
    }
}
