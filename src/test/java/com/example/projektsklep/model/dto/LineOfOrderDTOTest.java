package com.example.projektsklep.model.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.math.BigDecimal;

 class LineOfOrderDTOTest {

    @Test
     void testConstructorWithNullValues() {
        LineOfOrderDTO lineOfOrderDTO = new LineOfOrderDTO(null, null, null, null);

        Assertions.assertNull(lineOfOrderDTO.id());
        Assertions.assertNull(lineOfOrderDTO.productId());
        Assertions.assertEquals(0, lineOfOrderDTO.quantity());
        Assertions.assertEquals(BigDecimal.ZERO, lineOfOrderDTO.unitPrice());
    }

    @Test
     void testConstructorWithValues() {
        Long id = 1L;
        Long productId = 2L;
        Integer quantity = 3;
        BigDecimal unitPrice = new BigDecimal("10.50");

        LineOfOrderDTO lineOfOrderDTO = new LineOfOrderDTO(id, productId, quantity, unitPrice);

        Assertions.assertEquals(id, lineOfOrderDTO.id());
        Assertions.assertEquals(productId, lineOfOrderDTO.productId());
        Assertions.assertEquals(quantity, lineOfOrderDTO.quantity());
        Assertions.assertEquals(unitPrice, lineOfOrderDTO.unitPrice());
    }

    @Test
     void testQuantityDefaultHandling() {
        LineOfOrderDTO lineOfOrderDTO = new LineOfOrderDTO(1L, 2L, null, new BigDecimal("10.50"));

        Assertions.assertEquals(0, lineOfOrderDTO.quantity());
    }

    @Test
     void testUnitPriceDefaultHandling() {
        LineOfOrderDTO lineOfOrderDTO = new LineOfOrderDTO(1L, 2L, 3, null);

        Assertions.assertEquals(BigDecimal.ZERO, lineOfOrderDTO.unitPrice());
    }
}