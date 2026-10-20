package com.example.projektsklep.service;

import com.example.projektsklep.model.dto.AddressDTO;
import com.example.projektsklep.model.entities.order.LineOfOrder;
import com.example.projektsklep.model.entities.product.Product;
import com.example.projektsklep.model.entities.user.User;
import com.example.projektsklep.model.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class InventoryCheckoutTest {
    @Autowired OrderCheckoutService checkout;
    @Autowired ProductRepository products;
    @Autowired UserRepository users;
    @Autowired OrderRepository orders;
    private final AddressDTO address = new AddressDTO(null, "Street 1", "City", "00-001", "PL");

    private Product product(int stock) {
        var product = new Product("Inventory test", BigDecimal.TEN);
        product.setStockQuantity(stock);
        return products.saveAndFlush(product);
    }

    @Test
    void onlyOneBuyerCanPurchaseTheLastUnit() throws Exception {
        // Dwa rownolegle zakupy nie sprzedaja tej samej sztuki.
        var product = product(1);
        var user = users.saveAndFlush(new User());
        var pool = Executors.newFixedThreadPool(2);
        var start = new CountDownLatch(1);
        Callable<Boolean> buy = () -> {
            start.await();
            try {
                checkout.placeOrder(user.getId(), List.of(new LineOfOrder(product, 1)), address);
                return true;
            } catch (IllegalArgumentException ex) { return false; }
        };
        try {
            var first = pool.submit(buy);
            var second = pool.submit(buy);
            start.countDown();
            assertNotEquals(first.get(15, TimeUnit.SECONDS), second.get(15, TimeUnit.SECONDS));
            assertEquals(0, products.findById(product.getId()).orElseThrow().getStockQuantity());
            assertEquals(1, orders.findByAccountHolder_Id(user.getId()).size());
        } finally { pool.shutdownNow(); }
    }

    @Test
    void shortageRollsBackTheWholeOrderAndAllStockChanges() {
        // Brak drugiej pozycji cofa pobranie pierwszej.
        var first = product(5);
        var second = product(0);
        var user = users.saveAndFlush(new User());
        assertThrows(IllegalArgumentException.class, () -> checkout.placeOrder(user.getId(),
                List.of(new LineOfOrder(first, 2), new LineOfOrder(second, 1)), address));
        assertEquals(5, products.findById(first.getId()).orElseThrow().getStockQuantity());
        assertTrue(orders.findByAccountHolder_Id(user.getId()).isEmpty());
    }

    @Test
    void supplierDelayBlocksCheckoutDespitePhysicalStock() {
        // Produkt chwilowo niedostepny nie przechodzi finalizacji.
        var product = product(10);
        product.setUnavailableUntil(Instant.now().plusSeconds(300));
        product = products.saveAndFlush(product);
        var user = users.saveAndFlush(new User());
        var lines = List.of(new LineOfOrder(product, 1));
        assertThrows(IllegalArgumentException.class, () -> checkout.placeOrder(user.getId(), lines, address));
        assertEquals(10, products.findById(product.getId()).orElseThrow().getStockQuantity());
    }
}
