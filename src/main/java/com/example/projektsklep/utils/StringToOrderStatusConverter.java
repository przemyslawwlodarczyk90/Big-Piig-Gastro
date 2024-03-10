package com.example.projektsklep.utils;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import com.example.projektsklep.model.enums.OrderStatus;


@Component
class StringToOrderStatusConverter implements Converter<String, OrderStatus> {

    @Override
    public OrderStatus convert(String source) {
        try {
            return OrderStatus.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}