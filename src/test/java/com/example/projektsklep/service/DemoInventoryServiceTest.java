package com.example.projektsklep.service;

import com.example.projektsklep.model.entities.product.Product;
import com.example.projektsklep.model.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.*;
import java.util.List;
import java.util.random.RandomGenerator;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DemoInventoryServiceTest {
    private final Instant now = Instant.parse("2026-10-20T12:00:00Z");
    private final ProductRepository products = mock(ProductRepository.class);
    private final RandomGenerator random = mock(RandomGenerator.class);
    private final DemoInventoryService inventory = new DemoInventoryService(products, Clock.fixed(now, ZoneOffset.UTC), random);

    private Product product(int stock) {
        var product = new Product("Oven", BigDecimal.TEN);
        product.setStockQuantity(stock);
        return product;
    }

    @Test
    void lowStockReceivesRandomDelivery() {
        // Dostawa zwieksza stan ponizej progu.
        var product = product(4);
        when(products.findAllForUpdate()).thenReturn(List.of(product));
        when(random.nextInt(10, 31)).thenReturn(17);
        inventory.replenish();
        assertEquals(21, product.getStockQuantity());
        assertNull(product.getUnavailableUntil());
    }

    @Test
    void sufficientStockIsUntouched() {
        // Na progu 5 sztuk dostawa nie jest potrzebna.
        var product = product(5);
        when(products.findAllForUpdate()).thenReturn(List.of(product));
        inventory.replenish();
        assertEquals(5, product.getStockQuantity());
        verifyNoInteractions(random);
    }

    @Test
    void supplierDelayTemporarilyHidesOneProduct() {
        // Awaria dostawcy nie kasuje fizycznego zapasu.
        var first = product(10);
        var second = product(15);
        when(products.findAllForUpdate()).thenReturn(List.of(first, second));
        when(random.nextInt(2)).thenReturn(1);
        when(random.nextInt(2, 6)).thenReturn(3);
        inventory.replenish();
        assertEquals(10, first.availableStock(now));
        assertEquals(0, second.availableStock(now));
        assertEquals(15, second.getStockQuantity());
        assertEquals(15, second.availableStock(now.plusSeconds(180)));
    }

    @Test
    void activeDelayIsNotExtendedByScheduler() {
        // Kolejny tick nie odklada terminu powrotu produktu.
        var first = product(10);
        first.setUnavailableUntil(now.plusSeconds(120));
        when(products.findAllForUpdate()).thenReturn(List.of(first, product(10)));
        inventory.replenish();
        assertEquals(now.plusSeconds(120), first.getUnavailableUntil());
        verifyNoInteractions(random);
    }

    @Test
    void delayRotatesToAnotherProductAfterRecovery() {
        // Produkt po przerwie wraca, a kolejny czeka na dostawe.
        var first = product(10);
        first.setUnavailableUntil(now.minusSeconds(1));
        var second = product(10);
        when(products.findAllForUpdate()).thenReturn(List.of(first, second));
        when(random.nextInt(2, 6)).thenReturn(2);
        inventory.replenish();
        assertEquals(10, first.availableStock(now));
        assertEquals(0, second.availableStock(now));
    }

    @Test
    void completedRotationStartsAnotherCycle() {
        // Po obejscu katalogu mozna wylosowac nowy cykl.
        var first = product(10);
        var second = product(10);
        first.setUnavailableUntil(now.minusSeconds(1));
        second.setUnavailableUntil(now.minusSeconds(1));
        when(products.findAllForUpdate()).thenReturn(List.of(first, second));
        when(random.nextInt(2, 6)).thenReturn(2);
        inventory.replenish();
        assertEquals(0, first.availableStock(now));
        assertEquals(10, second.availableStock(now));
    }

    @Test
    void emptyCatalogIsSafe() {
        // Brak produktow nie powoduje losowania z pustej listy.
        when(products.findAllForUpdate()).thenReturn(List.of());
        inventory.replenish();
        verifyNoInteractions(random);
    }
}
