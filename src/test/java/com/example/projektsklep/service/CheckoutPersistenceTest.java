package com.example.projektsklep.service;

import com.example.projektsklep.model.dto.AddressDTO;
import com.example.projektsklep.model.entities.adress.Address;
import com.example.projektsklep.model.entities.product.Product;
import com.example.projektsklep.model.entities.user.User;
import com.example.projektsklep.model.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import({OrderCheckoutService.class, com.example.projektsklep.config.InventoryConfig.class})
class CheckoutPersistenceTest {
    @Autowired TestEntityManager em;
    @Autowired OrderCheckoutService checkout;
    @Autowired ProductRepository products;
    @Autowired OrderRepository orders;

    @Test
    void savesBasketLinesTotalAndAddressAndClearsBasket() {
        // Zapis i ponowny odczyt zachowuja pozycje, cene oraz adres.
        var user = new User();
        user.setEmail("checkout@example.com");
        user.setAddress(new Address(null, "Street 1", "City", "00-001", "PL"));
        em.persistAndFlush(user);
        var product = em.persistAndFlush(new Product("Oven", new BigDecimal("12.50")));
        product.setStockQuantity(10);
        var basket = new BasketService(products, checkout);
        basket.addProductToBasket(product.getId(), 2);
        // A separate product instance with changed fields must still merge by ID.
        product.setTitle("Updated oven");
        basket.addProductToBasket(product.getId(), 1);
        assertEquals(1, basket.prepareOrderForCheckout(user.getId()).lineOfOrders().size());
        basket.finalizeOrderAndRedirect(user.getId(), null, false);
        em.flush();
        em.clear();
        var saved = orders.findAll().get(0);
        assertEquals(1, saved.getLineOfOrders().size());
        assertEquals(3, saved.getLineOfOrders().get(0).getQuantity());
        assertEquals(saved.getId(), saved.getLineOfOrders().get(0).getOrder().getId());
        assertEquals(0, new BigDecimal("37.50").compareTo(saved.getTotalPrice()));
        assertEquals(7, products.findById(product.getId()).orElseThrow().getStockQuantity());
        assertEquals("Street 1", saved.getShippingAddress().getStreet());
        assertNotEquals(user.getAddress().getId(), saved.getShippingAddress().getId());
        assertTrue(basket.getCurrentBasket().getLineOfOrders().isEmpty());
        assertThrows(IllegalArgumentException.class, () -> basket.finalizeOrderAndRedirect(user.getId(), null, false));
        assertEquals(1, orders.count());
    }

    @Test
    void acceptsCustomAddressAndRejectsInvalidQuantities() {
        // Wlasny adres dziala, a niedozwolone ilosci nie trafiaja do koszyka.
        var user = em.persistAndFlush(new User());
        var product = em.persistAndFlush(new Product("Oven", BigDecimal.TEN));
        product.setStockQuantity(10);
        var basket = new BasketService(products, checkout);
        assertThrows(IllegalArgumentException.class, () -> basket.addProductToBasket(product.getId(), -1));
        basket.addProductToBasket(product.getId(), 2);
        basket.updateProductQuantity(product.getId(), 4);
        assertEquals(4, basket.prepareOrderForCheckout(user.getId()).lineOfOrders().get(0).quantity());
        basket.updateProductQuantity(product.getId(), 0);
        assertTrue(basket.getCurrentBasket().getLineOfOrders().isEmpty());
        basket.addProductToBasket(product.getId(), 1);
        basket.finalizeOrderAndRedirect(user.getId(), new AddressDTO(null, "Other 1", "City", "00-002", "PL"), true);
        em.clear();
        assertEquals("Other 1", orders.findAll().get(0).getShippingAddress().getStreet());
    }
}
