package com.example.projektsklep.model.dto;

import com.example.projektsklep.model.enums.ProductType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductDTOTest {

    @Test
    void productDTOValidFields() {
        ProductDTO productDTO = ProductDTO.builder()
                .title("Product Title")
                .description("Product Description")
                .price(new BigDecimal("10.00"))
                .productType(ProductType.DEFAULT_TYPE)
                .build();

        assertNotNull(productDTO.title(), "Title should not be null");
        assertFalse(productDTO.title().isBlank(), "Title should not be blank");
        assertNotNull(productDTO.description(), "Description should not be null");
        assertFalse(productDTO.description().isBlank(), "Description should not be blank");
        assertNotNull(productDTO.price(), "Price should not be null");
        assertTrue(productDTO.price().compareTo(BigDecimal.ZERO) >= 0, "Price should be greater than or equal to 0");
        assertNotNull(productDTO.productType(), "Product type should not be null");
    }

    @Test
    void productDTOTitleBlank() {
        ProductDTO productDTO = ProductDTO.builder()
                .title(" ")
                .description("Product Description")
                .price(new BigDecimal("10.00"))
                .productType(ProductType.DEFAULT_TYPE)
                .build();

        assertTrue(productDTO.title().isBlank(), "Title should be considered blank");
    }

    @Test
    void productDTONegativePrice() {
        ProductDTO productDTO = ProductDTO.builder()
                .title("Product Title")
                .description("Product Description")
                .price(new BigDecimal("-1.00"))
                .productType(ProductType.DEFAULT_TYPE)
                .build();

        assertTrue(productDTO.price().compareTo(BigDecimal.ZERO) < 0, "Price should be negative");
    }


}