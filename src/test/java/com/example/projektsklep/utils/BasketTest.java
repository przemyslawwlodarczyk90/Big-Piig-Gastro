package com.example.projektsklep.utils;

import com.example.projektsklep.model.entities.order.LineOfOrder;
import com.example.projektsklep.model.entities.product.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BasketTest {
    @Test
     void givenEmptyBasket_whenAddProduct_thenBasketHasOneLineOfOrder() {

        Basket basket = new Basket();
        Product product = new Product("Test Product", BigDecimal.valueOf(10));

        basket.addProduct(product, 2);

        List<LineOfOrder> lineOfOrders = basket.getLineOfOrders();
        assertEquals(1, lineOfOrders.size());
        LineOfOrder lineOfOrder = lineOfOrders.get(0);

        assertAll(
                () -> assertEquals(product, lineOfOrder.getProduct()),
                () -> assertEquals(2, lineOfOrder.getQuantity())
        );
    }
    @Test
     void givenBasketWithProduct_whenAddSameProduct_thenQuantityIncreases() {

        Basket basket = new Basket();
        Product product = new Product("Test Product", BigDecimal.valueOf(10));
        basket.addProduct(product, 1);


        basket.addProduct(product, 3);

        List<LineOfOrder> lineOfOrders = basket.getLineOfOrders();
        assertEquals(1, lineOfOrders.size());
        LineOfOrder lineOfOrder = lineOfOrders.get(0);

        assertAll(
                () -> assertEquals(product, lineOfOrder.getProduct()),
                () -> assertEquals(4, lineOfOrder.getQuantity())
        );
    }

    @Test
     void givenBasketWithOneProduct_whenRemoveProduct_thenBasketIsEmpty() {

        Basket basket = new Basket();
        Product product = new Product("Test Product", BigDecimal.valueOf(10));
        basket.addProduct(product, 1);

        basket.removeProduct(product);

        assertEquals(0, basket.getLineOfOrders().size());
    }

    @Test
     void givenBasketWithProductQuantity2_whenRemoveProduct_thenQuantityReducesTo1() {

        Basket basket = new Basket();
        Product product = new Product("Test Product", BigDecimal.valueOf(10));
        basket.addProduct(product, 2);

        basket.removeProduct(product);

        List<LineOfOrder> lineOfOrders = basket.getLineOfOrders();
        assertEquals(1, lineOfOrders.size());
        LineOfOrder lineOfOrder = lineOfOrders.get(0);

        assertAll(
                () -> assertEquals(product, lineOfOrder.getProduct()),
                () -> assertEquals(1, lineOfOrder.getQuantity())
        );
    }

    @Test
     void givenBasketWithProducts_whenClear_thenBasketIsEmpty() {
        // Given
        Basket basket = new Basket();
        basket.addProduct(new Product("Product 1", BigDecimal.valueOf(10)), 1);
        basket.addProduct(new Product("Product 2", BigDecimal.valueOf(5)), 2);

        // When
        basket.clear();

        // Then
        assertEquals(0, basket.getLineOfOrders().size());
    }

}