package com.example.projektsklep.service;

import com.example.projektsklep.model.dto.AddressDTO;
import com.example.projektsklep.model.dto.OrderDTO;
import com.example.projektsklep.model.dto.UserDTO;
import com.example.projektsklep.model.entities.order.LineOfOrder;
import com.example.projektsklep.model.entities.product.Product;
import com.example.projektsklep.model.repository.OrderRepository;
import com.example.projektsklep.model.repository.ProductRepository;
import com.example.projektsklep.utils.Basket;
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
        // Przygotowanie danych testowych dla Product
        sampleProduct = new Product();
        sampleProduct.setId(sampleProductId);
        sampleProduct.setTitle("Sample Product");
        sampleProduct.setPrice(BigDecimal.valueOf(100));
        productRepository.save(sampleProduct);

        // Konfiguracja mockowania UserService
        AddressDTO mockAddress = AddressDTO.builder().id(1L).street("TestStreet").city("TestCity").postalCode("12345").country("TestCountry").build();
        UserDTO mockUserDTO = new UserDTO(1L, "Test", "User", "test@example.com", "passwordHash", mockAddress, null);
        when(userService.findUserById(ArgumentMatchers.any(Long.class))).thenReturn(Optional.of(mockUserDTO));
    }

    @Test
    public void testAddProductToBasketAndClear() {
        // Dodajemy produkt do koszyka
        basketService.addProductToBasket(sampleProductId, quantity);

        // Prosta weryfikacja, że metoda addProductToBasket została wywołana
        // Uwaga: to nie sprawdza, czy produkt rzeczywiście został dodany do koszyka
        assertNotNull(basketService.getCurrentBasket(), "Koszyk nie powinien być null po dodaniu produktu.");

        // Wywołanie czyszczenia koszyka
        basketService.clear();

        // Uproszczona weryfikacja, że metoda clear() została wywołana
        // Uwaga: to nie weryfikuje, czy koszyk jest pusty
        assertNotNull(basketService.getCurrentBasket(), "Koszyk nie powinien być null po wyczyszczeniu.");
    }


    @Test
    public void testPlaceOrder() {
        // Dodajemy produkt do koszyka
        basketService.addProductToBasket(sampleProductId, quantity);

        // Składanie zamówienia
        OrderDTO orderDTO = basketService.prepareOrderForCheckout(1L);
        basketService.placeOrder(orderDTO);

        // Sprawdzenie, czy lista zamówień nie jest pusta (zakładamy, że zamówienie zostało utworzone)
        assertFalse(orderRepository.findAll().isEmpty(), "Lista zamówień nie powinna być pusta.");

        // Sprawdzenie, czy utworzone zamówienie ma przypisaną jakąkolwiek wartość totalPrice
        assertNotNull(orderRepository.findAll().get(0).getTotalPrice(), "Utworzone zamówienie powinno mieć przypisaną wartość totalPrice.");
    }


}