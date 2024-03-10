package com.example.projektsklep.utils;

import com.example.projektsklep.model.enums.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StringToOrderStatusConverterTest {

    private StringToOrderStatusConverter converter;

    @BeforeEach
    void setUp() {
        converter = new StringToOrderStatusConverter();
    }

    @Test
    void testConvertValidStatus() {
        assertEquals(OrderStatus.NEW_ORDER, converter.convert("NEW_ORDER"));
        assertEquals(OrderStatus.SHIPPED, converter.convert("shipped"));
    }

    @Test
    void testConvertInvalidStatus() {
        assertNull(converter.convert("invalid_status"));
    }


}