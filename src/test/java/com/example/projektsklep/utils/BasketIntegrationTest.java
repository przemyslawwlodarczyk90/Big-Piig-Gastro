//package com.example.projektsklep.utils;
//
//
//import com.example.projektsklep.model.entities.order.LineOfOrder;
//import com.example.projektsklep.model.entities.product.Product;
//import com.example.projektsklep.model.repository.ProductRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.anyLong;
//
//@RunWith(MockitoJUnitRunner.class)
//public class BasketIntegrationTest {
//
//    @Mock
//    private ProductRepository productRepository;
//
//    @InjectMocks
//    private Basket basket;
//
//    @Test
//    public void givenProductExists_whenAddProductToBasket_thenPriceIsUpdated() {
//        // Given
//        Product product = new Product("Test Product", BigDecimal.valueOf(10));
//        Mockito.when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
//
//        // When
//        basket.addProduct(product, 2);
//
//        // Then
//        List<LineOfOrder> lineOfOrders = basket.getLineOfOrders();
//        assertThat(lineOfOrders).hasSize(1);
//        LineOfOrder lineOfOrder = lineOfOrders.get(0);
//        assertThat(lineOfOrder.getProduct()).isEqualTo(product);
//        assertThat(lineOfOrder.getQuantity()).isEqualTo(2);
//        assertThat(lineOfOrder.getTotalPrice()).isEqualTo(BigDecimal.valueOf(20));
//    }
//
//    @Test
//    public void givenProductInBasket_whenRemoveProductFromBasket_thenPriceIsUpdated() {
//        // Given
//        Product product = new Product("Test Product", BigDecimal.valueOf(10));
//        basket.addProduct(product, 2);
//
//        // When
//        basket.removeProduct(product);
//
//        // Then
//        List<LineOfOrder> lineOfOrders = basket.getLineOfOrders();
//        assertThat(lineOfOrders).isEmpty();
//        assertThat(basket.getTotalPrice()).isEqualTo(BigDecimal.ZERO);
//    }
//}
