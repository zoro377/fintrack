package com.fintrack.service;

import com.fintrack.dto.ExpenseRequest;
import com.fintrack.dto.ExpenseResponse;
import com.fintrack.exceptions.ResourceNotFoundException;
import com.fintrack.model.Expense;
import com.fintrack.model.User;
import com.fintrack.repository.ExpenseRepository;
import com.fintrack.repository.UserRepository;
import com.fintrack.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    public ExpenseService(ExpenseRepository expenseRepository,
                          UserRepository userRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
    }

    public ExpenseResponse createExpense(ExpenseRequest request) {
        User user = getCurrentUser();
        Expense expense = Expense.builder()
                .user(user)
                .categoryId(request.getCategoryId())
                .amount(request.getAmount())
                .description(request.getDescription())
                .date(request.getDate())
                .paymentMode(request.getPaymentMode())
                .build();
        Expense saved = expenseRepository.save(expense);
        return toResponse(saved);
    }

    public List<ExpenseResponse> getExpenses() {
        User user = getCurrentUser();
        return expenseRepository.findAllByUser(user).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ExpenseResponse getExpenseById(Long id) {
        Expense expense = getExpenseForCurrentUser(id);
        return toResponse(expense);
    }

    public ExpenseResponse updateExpense(Long id, ExpenseRequest request) {
        Expense expense = getExpenseForCurrentUser(id);
        expense.setCategoryId(request.getCategoryId());
        expense.setAmount(request.getAmount());
        expense.setDescription(request.getDescription());
        expense.setDate(request.getDate());
        expense.setPaymentMode(request.getPaymentMode());
        return toResponse(expenseRepository.save(expense));
    }

    public void deleteExpense(Long id) {
        Expense expense = getExpenseForCurrentUser(id);
        expenseRepository.delete(expense);
    }

    private Expense getExpenseForCurrentUser(Long id) {
        User user = getCurrentUser();
        return expenseRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));
    }

    private User getCurrentUser() {
        String email = SecurityUtils.getCurrentUserEmail();
        if (email == null) {
            throw new ResourceNotFoundException("Authenticated user not found");
        }
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private ExpenseResponse toResponse(Expense expense) {
        return ExpenseResponse.builder()
                .id(expense.getId())
                .userId(expense.getUser().getId())
                .categoryId(expense.getCategoryId())
                .amount(expense.getAmount())
                .description(expense.getDescription())
                .date(expense.getDate())
                .paymentMode(expense.getPaymentMode())
                .build();
    }
}

