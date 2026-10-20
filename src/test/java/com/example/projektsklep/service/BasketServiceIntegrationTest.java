package com.example.projektsklep.service;

import com.example.projektsklep.model.entities.product.Product;
import com.example.projektsklep.model.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BasketServiceIntegrationTest {
    @Autowired BasketService basket;
    @Autowired ProductRepository products;

    @Test
    void basketsBelongToSeparateBrowserSessions() {
        // Dwie przegladarki nie dziela zawartosci koszyka.
        var product = new Product("Session test", BigDecimal.TEN);
        product.setStockQuantity(10);
        product = products.saveAndFlush(product);
        var first = new MockHttpServletRequest();
        var second = new MockHttpServletRequest();
        try {
            RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(first));
            basket.addProductToBasket(product.getId(), 2);
            RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(second));
            assertTrue(basket.getCurrentBasket().getLineOfOrders().isEmpty());
            RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(first));
            assertEquals(2, basket.getCurrentBasket().getLineOfOrders().get(0).getQuantity());
            basket.clear();
            assertTrue(basket.getCurrentBasket().getLineOfOrders().isEmpty());
        } finally {
            RequestContextHolder.resetRequestAttributes();
            products.deleteById(product.getId());
        }
    }
}
