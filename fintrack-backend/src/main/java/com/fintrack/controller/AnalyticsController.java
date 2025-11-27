package com.fintrack.controller;

import com.fintrack.dto.CategorySummaryResponse;
import com.fintrack.dto.MonthlySummaryResponse;
import com.fintrack.dto.PredictedExpenseResponse;
import com.fintrack.dto.TrendPointResponse;
import com.fintrack.dto.YearlySummaryResponse;
import com.fintrack.service.AnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/monthly-summary")
    public ResponseEntity<List<MonthlySummaryResponse>> monthlySummary() {
        return ResponseEntity.ok(analyticsService.getMonthlySummary());
    }

    @GetMapping("/yearly-summary")
    public ResponseEntity<List<YearlySummaryResponse>> yearlySummary() {
        return ResponseEntity.ok(analyticsService.getYearlySummary());
    }

    @GetMapping("/by-category")
    public ResponseEntity<List<CategorySummaryResponse>> categorySummary() {
        return ResponseEntity.ok(analyticsService.getCategorySummary());
    }

    @GetMapping("/trends")
    public ResponseEntity<List<TrendPointResponse>> trends() {
        return ResponseEntity.ok(analyticsService.getTrends());
    }

    @GetMapping("/predicted-expense")
    public ResponseEntity<PredictedExpenseResponse> predictedExpense() {
        return ResponseEntity.ok(analyticsService.getPredictedExpense());
    }
}

