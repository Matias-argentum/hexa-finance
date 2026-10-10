package com.mat.hexa_finance.infrastructure.adapters.in.web.transaction;

import com.mat.hexa_finance.application.ports.in.category.FindAllCategoriesUseCase;
import com.mat.hexa_finance.application.ports.in.dashboard.GetDashboardSummaryQuery;
import com.mat.hexa_finance.application.ports.in.transaction.*;
import com.mat.hexa_finance.application.ports.in.transaction.command.CreateTransactionCommand;
import com.mat.hexa_finance.application.ports.in.transaction.command.UpdateTransactionCommand;
import com.mat.hexa_finance.application.service.dashboard.DashboardSummaryService;
import com.mat.hexa_finance.domain.model.DashboardSummary;
import com.mat.hexa_finance.domain.model.Transaction;
import com.mat.hexa_finance.domain.model.TransactionType;
import com.mat.hexa_finance.infrastructure.adapters.in.web.auth.model.CustomUserDetails;
import com.mat.hexa_finance.infrastructure.adapters.in.web.category.dto.CategoryResponse;
import com.mat.hexa_finance.infrastructure.adapters.in.web.category.mapper.CategoryWebMapper;
import com.mat.hexa_finance.infrastructure.adapters.in.web.common.utils.HtmxUtils.HtmxUtils;
import com.mat.hexa_finance.infrastructure.adapters.in.web.transaction.dto.TransactionResponse;
import com.mat.hexa_finance.infrastructure.adapters.in.web.transaction.mapper.TransactionWebMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    private final FindAllCategoriesUseCase findAllCategoriesUseCase;
    private final SaveTransactionUseCase saveTransactionUseCase;
    private final GetTransactionUseCase getTransactionUseCase;
    private final FindAllTransactionsUseCase findAllTransactionsUseCase;
    private final UpdateTransactionUseCase updateTransactionUseCase;
    private final DeleteTransactionUseCase deleteTransactionUseCase;
    private final DashboardSummaryService dashboardSummaryService;

    public TransactionController(FindAllCategoriesUseCase findAllCategoriesUseCase,
                                 SaveTransactionUseCase saveTransactionUseCase,
                                 GetTransactionUseCase getTransactionUseCase,
                                 FindAllTransactionsUseCase findAllTransactionsUseCase,
                                 UpdateTransactionUseCase updateTransactionUseCase, DeleteTransactionUseCase deleteTransactionUseCase, DashboardSummaryService dashboardSummaryService) {
        this.findAllCategoriesUseCase = findAllCategoriesUseCase;
        this.saveTransactionUseCase = saveTransactionUseCase;
        this.getTransactionUseCase = getTransactionUseCase;
        this.findAllTransactionsUseCase = findAllTransactionsUseCase;
        this.updateTransactionUseCase = updateTransactionUseCase;
        this.deleteTransactionUseCase = deleteTransactionUseCase;
        this.dashboardSummaryService = dashboardSummaryService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/new")
    public String getCreateTransactionModal(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        List<CategoryResponse> categories = findAllCategoriesUseCase.executeSearch(user.getId())
                .stream().map(CategoryWebMapper::toResponse).toList();

        model.addAttribute("categories", categories);

        model.addAttribute("transaction", null);
        return "transactions/fragments/transactions-fragments :: transaction-modal";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public String createTransaction(
            @RequestParam BigDecimal amount,
            @RequestParam String type,
            @RequestParam String description,
            @RequestParam Long categoryId,
            @RequestParam LocalDate date,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletResponse response,
            Model model) {

        CreateTransactionCommand command = new CreateTransactionCommand();
        command.setAmount(amount);
        command.setType(TransactionType.valueOf(type));
        command.setDescription(description);
        command.setCategoryId(categoryId);
        command.setDate(date);
        command.setUserId(userDetails.getId());

        saveTransactionUseCase.execute(command);

        // Calcular el resumen para el mes de la transacción
        int year = date.getYear();
        int month = date.getMonthValue();
        DashboardSummary summary = dashboardSummaryService.execute(userDetails.getId(), month, year);

        model.addAttribute("username", userDetails.getUsername());
        model.addAttribute("summary", summary);
        model.addAttribute("selectedMonth", String.format("%d-%02d", year, month));

        // Envía los eventos para cerrar modal y disparar toast
        HtmxUtils.addNotificationAndClose(response, "Transacción registrada!");
        return "dashboard/dashboard :: dashboard-content";
    }

    /*
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/all")
    public String getTransactions(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        List<TransactionResponse> transactionsList = findAllTransactionsUseCase.execute(userDetails.getId())
                .stream().map(TransactionWebMapper::toResponse).toList();

        model.addAttribute("transactions", transactionsList);

        model.addAttribute("content", "transactions/transactions :: transactions-content");

        return "layouts/main-layout";
    }
    */

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public String getTransactionsPaginated(@RequestHeader(value = "HX-Request", required = false) String htmxHeader,
                                           @AuthenticationPrincipal CustomUserDetails userDetails,
                                           Model model,
                                           @RequestParam(required = true, defaultValue = "0") int page,
                                           @RequestParam(required = false) LocalDate from,
                                           @RequestParam(required = false) LocalDate to
    ) {

        if ((to == null || from == null) || to.isBefore(from)) {
            to = LocalDate.now();
            from = LocalDate.now().withDayOfMonth(1);
        }

        PageRequest pageRequest = PageRequest.of(page, 5);

        Page<TransactionResponse> transactionsPaginated = findAllTransactionsUseCase.executeForPagination(userDetails.getId(), from, to, pageRequest)
                .map(TransactionWebMapper::toResponse);

        model.addAttribute("page", transactionsPaginated);
        System.out.println("Total pages: " + transactionsPaginated.getTotalPages());
        System.out.println("Total elements: " + transactionsPaginated.getTotalElements());
        System.out.println("All content: " + transactionsPaginated.getContent());

        model.addAttribute("currentFrom", from);
        model.addAttribute("currentTo", to);

        if (htmxHeader != null) {
            return "transactions/transactions :: transactions-content";
        }

        model.addAttribute("content", "transactions/transactions :: transactions-content");

        return "layouts/main-layout";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/edit")
    public String getEditTransactionModal(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails user, Model model) {
        List<CategoryResponse> categories = findAllCategoriesUseCase.executeSearch(user.getId())
                .stream().map(CategoryWebMapper::toResponse).toList();

        Transaction transaction = getTransactionUseCase.executeFindById(id);

        model.addAttribute("categories", categories);
        model.addAttribute("transaction", transaction);

        return "transactions/fragments/transactions-fragments :: transaction-modal";
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public String updateTransaction(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model,
            @PathVariable Long id,
            @RequestParam BigDecimal amount,
            @RequestParam String type,
            @RequestParam String description,
            @RequestParam Long categoryId,
            @RequestParam LocalDate date,
            HttpServletResponse response
    ) {
        UpdateTransactionCommand command = new UpdateTransactionCommand();
        command.setId(id);
        command.setAmount(amount);
        command.setType(TransactionType.valueOf(type));
        command.setDescription(description);
        command.setCategoryId(categoryId);
        command.setDate(date);
        command.setUserId(userDetails.getId());

        updateTransactionUseCase.update(command);

        TransactionResponse transactionResponse = TransactionWebMapper.toResponse(
                getTransactionUseCase.executeFindByIdAndUserId(id, userDetails.getId())
        );

        model.addAttribute("t", transactionResponse);

        HtmxUtils.addNotificationAndClose(response, "Transacción actualizada!");

        return "transactions/fragments/transactions-fragments :: transaction-card";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/delete-confirm")
    public String getDeleteTransactionModalAlert(@PathVariable Long id,
                                                 @AuthenticationPrincipal CustomUserDetails userDetails,
                                                 Model model) {

        Transaction retrievedTransaction = getTransactionUseCase.executeFindById(id);

        //System.out.println(retrievedTransaction.getType());

        model.addAttribute("t", retrievedTransaction);
        return "transactions/fragments/transactions-fragments :: transaction-alert-modal";
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id,
                                               @AuthenticationPrincipal CustomUserDetails user,
                                               HttpServletResponse response) {

        deleteTransactionUseCase.delete(id, user.getId());
        HtmxUtils.addNotificationAndClose(response, "Transacción eliminada!");
        return ResponseEntity.ok().build();
    }
}