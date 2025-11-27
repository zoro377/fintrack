package com.fintrack.repository;

import com.fintrack.model.Expense;
import com.fintrack.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findAllByUser(User user);
    Optional<Expense> findByIdAndUser(Long id, User user);

    List<Expense> findAllByUserAndDateBetween(User user, LocalDate start, LocalDate end);
}

