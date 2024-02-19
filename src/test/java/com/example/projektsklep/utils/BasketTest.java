package com.example.projektsklep.utils;

import com.example.projektsklep.model.entities.order.LineOfOrder;
import com.example.projektsklep.model.entities.product.Product;
import org.assertj.core.api.AbstractFileAssert;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
//import static org.assertj.core.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BasketTest {
    @Test
    public void givenEmptyBasket_whenAddProduct_thenBasketHasOneLineOfOrder() {
        // Given
        Basket basket = new Basket();
        Product product = new Product("Test Product", BigDecimal.valueOf(10));

        // When
        basket.addProduct(product, 2);

        // Then
        List<LineOfOrder> lineOfOrders = basket.getLineOfOrders();
        assertEquals(1, lineOfOrders.size()); // Use assertEquals for size comparison
        LineOfOrder lineOfOrder = lineOfOrders.get(0);

        assertAll(
                // Use assertAll for multiple assertions on the same object
                () -> assertEquals(product, lineOfOrder.getProduct()),
                () -> assertEquals(2, lineOfOrder.getQuantity())
        );
    }
    @Test
    public void givenBasketWithProduct_whenAddSameProduct_thenQuantityIncreases() {
        // Given
        Basket basket = new Basket();
        Product product = new Product("Test Product", BigDecimal.valueOf(10));
        basket.addProduct(product, 1);

        // When
        basket.addProduct(product, 3);

        // Then
        List<LineOfOrder> lineOfOrders = basket.getLineOfOrders();
        assertEquals(1, lineOfOrders.size());
        LineOfOrder lineOfOrder = lineOfOrders.get(0);

        assertAll(
                () -> assertEquals(product, lineOfOrder.getProduct()),
                () -> assertEquals(4, lineOfOrder.getQuantity())
        );
    }


    @Test
    public void givenBasketWithOneProduct_whenRemoveProduct_thenBasketIsEmpty() {
        // Given
        Basket basket = new Basket();
        Product product = new Product("Test Product", BigDecimal.valueOf(10));
        basket.addProduct(product, 1);

        // When
        basket.removeProduct(product);

        // Then
        assertEquals(0, basket.getLineOfOrders().size()); // Use assertEquals for size comparison
    }

    @Test
    public void givenBasketWithProductQuantity2_whenRemoveProduct_thenQuantityReducesTo1() {
        // Given
        Basket basket = new Basket();
        Product product = new Product("Test Product", BigDecimal.valueOf(10));
        basket.addProduct(product, 2);

        // When
        basket.removeProduct(product);

        // Then
        List<LineOfOrder> lineOfOrders = basket.getLineOfOrders();
        assertEquals(1, lineOfOrders.size());
        LineOfOrder lineOfOrder = lineOfOrders.get(0);

        assertAll(
                () -> assertEquals(product, lineOfOrder.getProduct()), // Use assertEquals for product comparison
                () -> assertEquals(1, lineOfOrder.getQuantity())
        );
    }

    @Test
    public void givenBasketWithProducts_whenClear_thenBasketIsEmpty() {
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