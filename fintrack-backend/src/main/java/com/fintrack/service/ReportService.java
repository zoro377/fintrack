package com.fintrack.service;

import com.fintrack.exceptions.ResourceNotFoundException;
import com.fintrack.model.Expense;
import com.fintrack.model.User;
import com.fintrack.repository.CategoryRepository;
import com.fintrack.repository.ExpenseRepository;
import com.fintrack.repository.UserRepository;
import com.fintrack.utils.SecurityUtils;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ReportService {

    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public ReportService(ExpenseRepository expenseRepository,
                         CategoryRepository categoryRepository,
                         UserRepository userRepository) {
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public ByteArrayInputStream generateCsvReport() {
        User user = getCurrentUser();
        List<Expense> expenses = expenseRepository.findAllByUser(user);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(out);
        writer.println("Date,Category,Amount,Payment Mode,Description");
        for (Expense expense : expenses) {
            writer.printf("%s,%s,%s,%s,%s%n",
                    expense.getDate().format(DATE_FORMATTER),
                    resolveCategory(expense.getCategoryId()),
                    expense.getAmount(),
                    expense.getPaymentMode(),
                    sanitize(expense.getDescription()));
        }
        writer.flush();
        return new ByteArrayInputStream(out.toByteArray());
    }

    public ByteArrayInputStream generatePdfReport() {
        User user = getCurrentUser();
        List<Expense> expenses = expenseRepository.findAllByUser(user);
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, out);
            document.open();
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            document.add(new Paragraph("Expense Report", titleFont));
            document.add(new Paragraph(" "));
            PdfPTable table = new PdfPTable(5);
            addHeader(table, "Date");
            addHeader(table, "Category");
            addHeader(table, "Amount");
            addHeader(table, "Payment Mode");
            addHeader(table, "Description");
            for (Expense expense : expenses) {
                table.addCell(expense.getDate().format(DATE_FORMATTER));
                table.addCell(resolveCategory(expense.getCategoryId()));
                table.addCell(formatAmount(expense.getAmount()));
                table.addCell(expense.getPaymentMode());
                table.addCell(expense.getDescription() == null ? "" : expense.getDescription());
            }
            document.add(table);
            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Failed to create PDF report", e);
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

    private void addHeader(PdfPTable table, String text) {
        PdfPCell header = new PdfPCell();
        header.setPhrase(new Paragraph(text));
        table.addCell(header);
    }

    private String formatAmount(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.HALF_UP).toString();
    }

    private String resolveCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .map(category -> category.getName())
                .orElse("Unknown");
    }

    private String sanitize(String value) {
        if (value == null) {
            return "";
        }
        return value.replace(",", " ");
    }

    private User getCurrentUser() {
        String email = SecurityUtils.getCurrentUserEmail();
        if (email == null) {
            throw new ResourceNotFoundException("Authenticated user not found");
        }
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}

