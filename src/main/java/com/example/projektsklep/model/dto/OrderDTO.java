package com.example.projektsklep.model.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import javax.validation.constraints.NotNull;

@Builder
public record OrderDTO(
        Long id,
        @NotNull Long userId,
        @NotNull  String orderStatus,
        LocalDate dateCreated,
        LocalDate sentAt,
        BigDecimal totalPrice,
        @NotNull List<LineOfOrderDTO> lineOfOrders,
        AddressDTO shippingAddress
) {}