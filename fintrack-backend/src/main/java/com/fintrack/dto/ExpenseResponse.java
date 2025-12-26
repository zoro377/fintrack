package com.fintrack.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseResponse {
    private final Long id;
    private final Long userId;
    private final Long categoryId;
    private final BigDecimal amount;
    private final String description;
    private final LocalDate date;
    private final String paymentMode;

    public ExpenseResponse(Long id,
                           Long userId,
                           Long categoryId,
                           BigDecimal amount,
                           String description,
                           LocalDate date,
                           String paymentMode) {
        this.id = id;
        this.userId = userId;
        this.categoryId = categoryId;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.paymentMode = paymentMode;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getPaymentMode() {
        return paymentMode;
    }
}

