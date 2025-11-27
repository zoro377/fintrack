package com.fintrack.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;

@Value
@Builder
public class TrendPointResponse {
    LocalDate date;
    BigDecimal total;
}

