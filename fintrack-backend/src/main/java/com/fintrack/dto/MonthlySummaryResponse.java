package com.fintrack.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class MonthlySummaryResponse {
    int year;
    int month;
    BigDecimal total;
}

