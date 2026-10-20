package com.example.projektsklep.service;

import com.example.projektsklep.model.entities.adress.Address;
import com.example.projektsklep.model.entities.order.*;
import com.example.projektsklep.model.entities.product.Product;
import com.example.projektsklep.model.entities.user.User;
import com.example.projektsklep.model.enums.OrderStatus;
import com.example.projektsklep.model.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;
import org.springframework.ui.ExtendedModelMap;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {
    private final OrderRepository orders = mock(OrderRepository.class);
    private final UserRepository users = mock(UserRepository.class);
    private final OrderService service = new OrderService(orders, users);

    private Order order() {
        var order = new Order(); order.setId(1L);
        var user = new User(); user.setId(7L); order.setAccountHolder(user);
        order.setOrderStatus(OrderStatus.NEW_ORDER); order.setDateCreated(LocalDate.now());
        order.setShippingAddress(new Address(2L, "Street", "City", "00-001", "PL"));
        var product = new Product("Oven", BigDecimal.TEN); product.setId(3L);
        order.getLineOfOrders().add(new LineOfOrder(product, 2)); order.calculateTotalPrice();
        return order;
    }

    @Test
    void mapsOrderDetailsAndUserHistory() {
        // Historia zawiera pozycje, adres i wlasciciela zamowienia.
        var order = order();
        when(orders.findById(1L)).thenReturn(Optional.of(order));
        when(orders.findByAccountHolder_Id(7L)).thenReturn(List.of(order));
        when(users.findByEmail("buyer@example.com")).thenReturn(Optional.of(order.getAccountHolder()));
        var dto = service.findOrderDTOById(1L).orElseThrow();
        assertEquals(7L, dto.userId()); assertEquals("Street", dto.shippingAddress().street());
        assertEquals(3L, dto.lineOfOrders().get(0).productId());
        assertEquals(new BigDecimal("20"), dto.totalPrice());
        assertEquals(1, service.findOrdersForUserEmail("buyer@example.com").size());
        assertThrows(com.example.projektsklep.exception.UserNotFoundException.class, () -> service.findOrdersForUserEmail("missing"));
        assertTrue(service.findOrderDTOById(99L).isEmpty());
        order.setAccountHolder(null); order.setShippingAddress(null); order.setLineOfOrders(null); order.setOrderStatus(null);
        var legacy = service.findOrderDTOById(1L).orElseThrow();
        assertEquals("UNDEFINED", legacy.orderStatus()); assertNull(legacy.userId());
        assertNull(legacy.shippingAddress()); assertTrue(legacy.lineOfOrders().isEmpty());
    }

    @Test
    void paginationAndStatusFormsExposeCorrectNavigation() {
        // Srodkowa strona ma poprawne linki poprzednia/nastepna.
        var order = order(); var page = PageRequest.of(1, 1);
        when(orders.findAll(page)).thenReturn(new PageImpl<>(List.of(order), page, 3));
        var model = service.findAllOrdersWithPagination(page);
        assertEquals(0, model.get("previousPage")); assertEquals(2, model.get("nextPage"));
        assertEquals(3, model.get("totalPages"));
        var single = PageRequest.of(0, 10);
        when(orders.findAll(single)).thenReturn(new PageImpl<>(List.of(order)));
        assertEquals(0, service.findAllOrdersWithPagination(single).get("nextPage"));
        when(orders.findById(1L)).thenReturn(Optional.of(order));
        var form = new ExtendedModelMap(); service.prepareEditOrderFormModel(1L, form);
        assertTrue(form.containsAttribute("statuses"));
        assertThrows(com.example.projektsklep.exception.OrderNotFoundException.class, () -> service.prepareEditOrderFormModel(99L, form));
        when(orders.findAllByOrderStatus(OrderStatus.NEW_ORDER)).thenReturn(List.of(order));
        service.prepareOrdersByStatusModel(null, form);
        assertEquals("NEW_ORDER", form.get("selectedStatus"));
        service.updateOrderStatus(1L, "new_order"); verify(orders).save(order);
        assertThrows(IllegalArgumentException.class, () -> service.updateOrderStatus(1L, "invalid"));
        assertThrows(RuntimeException.class, () -> service.updateOrderStatus(99L, "new_order"));
    }
}
