package com.mat.hexa_finance.infrastructure.adapters.in.web.recurring_transaction;

import com.mat.hexa_finance.application.ports.in.category.FindAllCategoriesUseCase;
import com.mat.hexa_finance.application.ports.in.recurring_transaction.*;
import com.mat.hexa_finance.application.ports.in.recurring_transaction.command.CreateRecurringTransactionCommand;
import com.mat.hexa_finance.application.ports.in.recurring_transaction.command.UpdateRecurringTransactionCommand;
import com.mat.hexa_finance.domain.model.RecurringTransaction;
import com.mat.hexa_finance.domain.model.RecurringTransactionDetail;
import com.mat.hexa_finance.domain.model.TransactionType;
import com.mat.hexa_finance.infrastructure.adapters.in.web.auth.model.CustomUserDetails;
import com.mat.hexa_finance.infrastructure.adapters.in.web.category.dto.CategoryResponse;
import com.mat.hexa_finance.infrastructure.adapters.in.web.category.mapper.CategoryWebMapper;
import com.mat.hexa_finance.infrastructure.adapters.in.web.common.utils.HtmxUtils.HtmxUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/recurring")
public class RecurringTransactionController {
    private final FindAllRecurringTransactionsUseCase findAllRecurringTransactionsUseCase;
    private final SaveRecurringTransactionUserCase saveRecurringTransactionUserCase;
    private final DeleteRecurringTransactionUseCase deleteRecurringTransactionUseCase;
    private final UpdateRecurringTransactionUseCase updateRecurringTransactionUseCase;
    private final GetRecurringTransactionUseCase getRecurringTransactionUseCase;
    private final FindAllCategoriesUseCase findAllCategoriesUseCase;
    private final ToggleRecurringTransactionStatus toggleRecurringTransactionStatus;

    public RecurringTransactionController(FindAllRecurringTransactionsUseCase findAllRecurringTransactionsUseCase,
                                          SaveRecurringTransactionUserCase saveRecurringTransactionUserCase,
                                          DeleteRecurringTransactionUseCase deleteRecurringTransactionUseCase,
                                          UpdateRecurringTransactionUseCase updateRecurringTransactionUseCase,
                                          GetRecurringTransactionUseCase getRecurringTransactionUseCase, FindAllCategoriesUseCase findAllCategoriesUseCase, ToggleRecurringTransactionStatus toggleRecurringTransactionStatus) {
        this.findAllRecurringTransactionsUseCase = findAllRecurringTransactionsUseCase;
        this.saveRecurringTransactionUserCase = saveRecurringTransactionUserCase;
        this.deleteRecurringTransactionUseCase = deleteRecurringTransactionUseCase;
        this.updateRecurringTransactionUseCase = updateRecurringTransactionUseCase;
        this.getRecurringTransactionUseCase = getRecurringTransactionUseCase;
        this.findAllCategoriesUseCase = findAllCategoriesUseCase;
        this.toggleRecurringTransactionStatus = toggleRecurringTransactionStatus;
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public String getRecurringTransactions(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        List<RecurringTransactionDetail> recurringTransactions = findAllRecurringTransactionsUseCase.execute(userDetails.getId());

        model.addAttribute("recurringTransactions", recurringTransactions);

        model.addAttribute("content", "recurringTransactions/recurringTransactions :: recurringTransactions-content");

        return "layouts/main-layout";
    }

    @PreAuthorize("isAuthenticated")
    @GetMapping("/new")
    public String getNewRecurringTransactionModal(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        List<CategoryResponse> categories = findAllCategoriesUseCase.executeSearch(userDetails.getId())
                .stream().map(CategoryWebMapper::toResponse).toList();
        model.addAttribute("categories", categories);
        model.addAttribute("recurringTransaction", null);

        return "recurringTransactions/fragments/recurring-transactions-fragments :: recurring-transaction-modal";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public String createRecurringTransaction(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            CreateRecurringTransactionCommand command,
            HttpServletResponse response) {

        command.setUserId(userDetails.getId());

        try {

            saveRecurringTransactionUserCase.execute(command);


            HtmxUtils.addNotification(response, "Transacción recurrente creada correctamente");

            response.setHeader("HX-Location", "/recurring");

            return null;

        } catch (Exception e) {
            HtmxUtils.addNotification(response, "Error al crear la transacción: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/delete-confirm")
    public String getDeleteRecurringTransactionModal(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails, Model model) {

        RecurringTransactionDetail retrievedRecurringTransaction = getRecurringTransactionUseCase.executeFindByIdAndUserId(id, userDetails.getId());

        model.addAttribute("rt", retrievedRecurringTransaction);

        return "recurringTransactions/fragments/recurring-transaction-alert-modal :: recurring-transaction-alert-modal";
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecurringTransaction(@PathVariable Long id,
                                                           @AuthenticationPrincipal CustomUserDetails userDetails,
                                                           HttpServletResponse response) {
        System.out.println("Entre????");
        deleteRecurringTransactionUseCase.delete(id, userDetails.getId());
        HtmxUtils.addNotificationAndClose(response, "Transacción recurrente eliminada!");

        return ResponseEntity.ok().build();

    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{id}/toggle")
    public String toggleRecurringTransaction(@PathVariable Long id,
                                             @AuthenticationPrincipal CustomUserDetails userDetails,
                                             Model model) {

        RecurringTransactionDetail updatedRT = toggleRecurringTransactionStatus.execute(id, userDetails.getId());

        model.addAttribute("rt", updatedRT);

        return "recurringTransactions/fragments/recurring-transaction-card :: recurring-card(rt=${rt})";
    }

    @GetMapping("/{id}/edit")
    public String getEditModal(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        RecurringTransactionDetail rt = getRecurringTransactionUseCase.executeFindByIdAndUserId(id, userDetails.getId());
        List<CategoryResponse> categories = findAllCategoriesUseCase.executeSearch(userDetails.getId())
                .stream().map(CategoryWebMapper::toResponse).toList();
        model.addAttribute("categories", categories);

        model.addAttribute("recurringTransaction", rt);
        return "recurringTransactions/fragments/recurring-transactions-fragments :: recurring-transaction-modal";
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public String updateRecurringTransaction(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("id") Long rtId,
            UpdateRecurringTransactionCommand command,
            HttpServletResponse response) {

        command.setUserId(userDetails.getId());
        command.setId(rtId);


        RecurringTransaction rt = updateRecurringTransactionUseCase.update(command);


        HtmxUtils.addNotification(response, "Transacción recurrente actualizada correctamente");

        response.setHeader("HX-Location", "/recurring");

        return null;

    }




}
