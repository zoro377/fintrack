package com.fintrack.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class CategorySummaryResponse {
    Long categoryId;
    String categoryName;
    BigDecimal total;
}

