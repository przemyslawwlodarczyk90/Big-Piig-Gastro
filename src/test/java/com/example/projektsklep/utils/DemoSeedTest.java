package com.example.projektsklep.utils;

import com.example.projektsklep.model.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {"app.demo-data.enabled=true", "spring.datasource.url=jdbc:h2:mem:demoseed;DB_CLOSE_DELAY=-1"})
class DemoSeedTest {
    @Autowired DbInit init;
    @Autowired ProductRepository products;
    @Autowired UserRepository users;
    @Autowired OrderRepository orders;

    @Test
    void explicitDemoSeedCreatesCatalogOnlyOnce() {
        // Dane demonstracyjne powstaja raz i nie mnoza sie po restarcie.
        assertEquals(100, products.count()); assertEquals(2, users.count()); assertEquals(20, orders.count());
        ReflectionTestUtils.invokeMethod(init, "postConstruct");
        assertEquals(100, products.count()); assertEquals(2, users.count()); assertEquals(20, orders.count());
        assertTrue(users.findByEmail("user@example.com").isPresent());
    }
}
