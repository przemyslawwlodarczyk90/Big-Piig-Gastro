package com.example.projektsklep.model.repository;

import com.example.projektsklep.model.entities.order.Order;
import com.example.projektsklep.model.enums.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;






import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;


@DataJpaTest
public class OrderRepositoryIntegrationTest {

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {

        orderRepository.deleteAll();
        Order order = new Order();
        order.setOrderStatus(OrderStatus.NEW_ORDER);
        orderRepository.save(order);
    }

    @Test
    void testFindByOrderStatus() {
        List<Order> orders = orderRepository.findAllByOrderStatus(OrderStatus.NEW_ORDER);
        assertFalse(orders.isEmpty(), "The order list should not be empty.");
    }

    @Test
    void testFindAllPageable() {
        var pageable = PageRequest.of(0, 10);
        var pageResult = orderRepository.findAll(pageable);
        assertFalse(pageResult.isEmpty(), "The paged result should not be empty.");
    }
}