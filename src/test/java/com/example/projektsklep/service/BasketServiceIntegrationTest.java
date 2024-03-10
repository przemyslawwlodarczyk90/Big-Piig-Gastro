package com.example.projektsklep.service;

import com.example.projektsklep.model.dto.AddressDTO;
import com.example.projektsklep.model.dto.OrderDTO;
import com.example.projektsklep.model.dto.UserDTO;
import com.example.projektsklep.model.entities.product.Product;
import com.example.projektsklep.model.repository.OrderRepository;
import com.example.projektsklep.model.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Testcontainers
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Import(BasketService.class)
public class BasketServiceIntegrationTest {

    @Container
    public static PostgreSQLContainer container = new PostgreSQLContainer("postgres:14-alpine")
            .withUsername("user")
            .withPassword("password")
            .withDatabaseName("pgDB");

    @DynamicPropertySource
    static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);
    }

    @Autowired
    private BasketService basketService;

    @MockBean
    private UserService userService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    private Product sampleProduct;
    private final Long sampleProductId = 1L;
    private final int quantity = 5;

    @BeforeEach
    public void setup() {

        sampleProduct = new Product();
        sampleProduct.setId(sampleProductId);
        sampleProduct.setTitle("Sample Product");
        sampleProduct.setPrice(BigDecimal.valueOf(100));
        productRepository.save(sampleProduct);


        AddressDTO mockAddress = AddressDTO.builder().id(1L).street("TestStreet").city("TestCity").postalCode("12345").country("TestCountry").build();
        UserDTO mockUserDTO = new UserDTO(1L, "Test", "User", "test@example.com", "passwordHash", mockAddress, null);
        when(userService.findUserById(ArgumentMatchers.any(Long.class))).thenReturn(Optional.of(mockUserDTO));
    }

    @Test
     void testAddProductToBasketAndClear() {

        basketService.addProductToBasket(sampleProductId, quantity);

        assertNotNull(basketService.getCurrentBasket(), "The shopping cart should not be null after adding a product.");

        basketService.clear();

        assertNotNull(basketService.getCurrentBasket(), "The shopping cart should not be null after clearing.");
    }

    @Test
     void testPlaceOrder() {

        basketService.addProductToBasket(sampleProductId, quantity);

        OrderDTO orderDTO = basketService.prepareOrderForCheckout(1L);
        basketService.placeOrder(orderDTO);

        assertFalse(orderRepository.findAll().isEmpty(), "The order list should not be empty.");

        assertNotNull(orderRepository.findAll().get(0).getTotalPrice(), "The created order should have a totalPrice value assigned to it.");
    }

}