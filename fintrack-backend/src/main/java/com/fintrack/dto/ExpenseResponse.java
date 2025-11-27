package com.fintrack.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;

@Value
@Builder
public class ExpenseResponse {
    Long id;
    Long userId;
    Long categoryId;
    BigDecimal amount;
    String description;
    LocalDate date;
    String paymentMode;
}

