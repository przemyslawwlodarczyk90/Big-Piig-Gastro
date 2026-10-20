package com.example.projektsklep.controller;

import com.example.projektsklep.model.entities.adress.Address;
import com.example.projektsklep.model.entities.product.Product;
import com.example.projektsklep.model.entities.user.User;
import com.example.projektsklep.model.enums.AdminOrUser;
import com.example.projektsklep.model.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.mock.web.MockHttpSession;
import java.math.BigDecimal;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class StorefrontFlowTest {
    @Autowired MockMvc mvc;
    @Autowired ProductRepository products;
    @Autowired UserRepository users;
    @Autowired RoleRepository roles;
    @Autowired OrderRepository orders;
    @Autowired PasswordEncoder passwords;

    @Test
    void loginBasketAndCheckoutRenderPagesAndReduceStock() throws Exception {
        // Pelny zakup przez formularze zapisuje zamowienie i pomniejsza stan.
        var user = new User(); user.setEmail("flow@example.com"); user.setFirstName("Flow");
        user.setLastName("Buyer"); user.setPasswordHash(passwords.encode("secret"));
        user.setRoles(Set.of(roles.findByRoleType(AdminOrUser.USER).orElseThrow()));
        user.setAddress(new Address(null, "Street 1", "City", "00-001", "PL"));
        user = users.saveAndFlush(user);
        var product = new Product("Flow oven", BigDecimal.TEN);
        product.setStockQuantity(3); product = products.saveAndFlush(product);
        mvc.perform(get("/user/login")).andExpect(status().isOk()).andExpect(content().string(containsString("_csrf")));
        var login = mvc.perform(post("/user/login").with(csrf()).param("username", user.getEmail()).param("password", "secret"))
                .andExpect(redirectedUrl("/home")).andReturn();
        var jwt = login.getResponse().getCookie("ACCESS_TOKEN");
        var session = new MockHttpSession();
        mvc.perform(get("/api/productDetails/" + product.getId()).cookie(jwt))
                .andExpect(status().isOk()).andExpect(content().string(containsString("Dostepne sztuki: 3")));
        mvc.perform(post("/basket/add/" + product.getId()).session(session).cookie(jwt).with(csrf()).param("quantity", "2"))
                .andExpect(redirectedUrl("/account/basket"));
        mvc.perform(get("/account/basket").session(session).cookie(jwt))
                .andExpect(status().isOk()).andExpect(content().string(containsString("Flow oven")));
        mvc.perform(get("/account/checkoutBasket").session(session).cookie(jwt)).andExpect(status().isOk());
        mvc.perform(post("/account/checkoutBasket").session(session).cookie(jwt).with(csrf()))
                .andExpect(redirectedUrl("/account/orderSuccess"));
        assertEquals(1, products.findById(product.getId()).orElseThrow().getStockQuantity());
        var order = orders.findByAccountHolder_Id(user.getId()).get(0);
        mvc.perform(get("/account/orders/" + order.getId()).cookie(jwt)).andExpect(status().isOk())
                .andExpect(content().string(containsString("Produkt #" + product.getId())));
        mvc.perform(get("/account/basket").session(session).cookie(jwt)).andExpect(status().isOk())
                .andExpect(content().string(containsString("Twój koszyk jest pusty")));
    }
}
