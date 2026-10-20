package com.example.projektsklep.controller;

import com.example.projektsklep.model.dto.UserDTO;
import com.example.projektsklep.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CheckoutControllerTest {
    @Test
    void anotherUsersOrderReturns404() throws Exception {
        var users = mock(UserService.class);
        var orders = mock(OrderService.class);
        when(users.findUserByEmail("buyer@example.com")).thenReturn(Optional.of(UserDTO.builder().id(7L).build()));
        when(orders.findOrderDTOById(1L)).thenReturn(Optional.of(
                com.example.projektsklep.model.dto.OrderDTO.builder().id(1L).userId(8L).build()));
        var mvc = MockMvcBuilders.standaloneSetup(new UserAccountController(orders, users, mock(BasketService.class))).build();
        mvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/account/orders/1")
                        .principal(org.springframework.security.authentication.UsernamePasswordAuthenticationToken.authenticated(
                                "buyer@example.com", null, java.util.List.of())))
                .andExpect(status().isNotFound());
    }

    @Test
    void checkoutUsesAuthenticatedUserAndActuallySaves() throws Exception {
        var users = mock(UserService.class);
        var basket = mock(BasketService.class);
        when(users.findUserByEmail("buyer@example.com")).thenReturn(Optional.of(
                UserDTO.builder().id(7L).build()));
        when(basket.finalizeOrderAndRedirect(eq(7L), any(), eq(false)))
                .thenReturn("redirect:/account/orderSuccess");
        var mvc = MockMvcBuilders.standaloneSetup(new UserAccountController(mock(OrderService.class), users, basket)).build();
        mvc.perform(post("/account/checkoutBasket").principal(
                        org.springframework.security.authentication.UsernamePasswordAuthenticationToken.authenticated(
                                "buyer@example.com", null, java.util.List.of()))
                        .param("userId", "999").param("totalPrice", "0"))
                .andExpect(redirectedUrl("/account/orderSuccess"));
        verify(basket).finalizeOrderAndRedirect(eq(7L), any(), eq(false));
    }
}
