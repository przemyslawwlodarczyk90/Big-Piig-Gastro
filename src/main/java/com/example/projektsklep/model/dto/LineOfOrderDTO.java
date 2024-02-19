package com.example.projektsklep.model.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record LineOfOrderDTO(
        Long id,
        Long productId,
        Integer quantity,
        BigDecimal unitPrice
) {
    public LineOfOrderDTO(Long id, Long productId, Integer quantity, BigDecimal unitPrice) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity != null ? quantity : 0;
        this.unitPrice = unitPrice != null ? unitPrice : BigDecimal.ZERO;
    }
}