package com.example.projektsklep.model.repository;

import com.example.projektsklep.model.entities.order.Order;
import com.example.projektsklep.model.enums.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

@Testcontainers
@SpringBootTest
public class OrderRepositoryIntegrationTest {

    @Container
    public static PostgreSQLContainer container = new PostgreSQLContainer("postgres:14-alpine")
            .withUsername("user")
            .withPassword("password")
            .withDatabaseName("pgDB");


    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);
    }

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        // Czyszczenie bazy danych i przygotowanie danych testowych
        orderRepository.deleteAll();
        Order order = new Order();
        order.setOrderStatus(OrderStatus.NEW_ORDER);
        orderRepository.save(order);
    }

    @Test
    void testFindByOrderStatus() {
        List<Order> orders = orderRepository.findAllByOrderStatus(OrderStatus.NEW_ORDER);
        assertFalse(orders.isEmpty(), "Lista zamówień nie powinna być pusta.");
    }

    @Test
    void testFindAllPageable() {
        var pageable = PageRequest.of(0, 10);
        var pageResult = orderRepository.findAll(pageable);
        assertFalse(pageResult.isEmpty(), "Stronicowany wynik nie powinien być pusty.");
    }
}