package com.mat.hexa_finance.application.service.recurring_transaction;

import com.mat.hexa_finance.application.ports.in.recurring_transaction.*;
import com.mat.hexa_finance.application.ports.in.recurring_transaction.command.CreateRecurringTransactionCommand;
import com.mat.hexa_finance.application.ports.in.recurring_transaction.command.UpdateRecurringTransactionCommand;
import com.mat.hexa_finance.application.ports.out.recurring_transaction.DeleteRecurringTransactionPort;
import com.mat.hexa_finance.application.ports.out.recurring_transaction.LoadRecurringTransactionsPort;
import com.mat.hexa_finance.application.ports.out.recurring_transaction.SaveRecurringTransactionPort;
import com.mat.hexa_finance.domain.exception.BadTransactionFieldException;
import com.mat.hexa_finance.domain.exception.RecurringTransactionNotFoundException;
import com.mat.hexa_finance.domain.model.RecurringTransaction;
import com.mat.hexa_finance.domain.model.RecurringTransactionDetail;

import java.math.BigDecimal;
import java.util.List;

public class RecurringTransactionService implements SaveRecurringTransactionUserCase,
        FindAllRecurringTransactionsUseCase,
        GetRecurringTransactionUseCase,
        DeleteRecurringTransactionUseCase,
        UpdateRecurringTransactionUseCase {

    private final DeleteRecurringTransactionPort deleteRecurringTransactionPort;
    private final SaveRecurringTransactionPort saveRecurringTransactionPort;
    private final LoadRecurringTransactionsPort loadRecurringTransactionsPort;

    public RecurringTransactionService(DeleteRecurringTransactionPort deleteRecurringTransactionPort, SaveRecurringTransactionPort saveRecurringTransactionPort, LoadRecurringTransactionsPort loadRecurringTransactionsPort) {
        this.deleteRecurringTransactionPort = deleteRecurringTransactionPort;
        this.saveRecurringTransactionPort = saveRecurringTransactionPort;
        this.loadRecurringTransactionsPort = loadRecurringTransactionsPort;
    }

    @Override
    public void delete(Long id, Long userId) {
        RecurringTransactionDetail foundTransaction = loadRecurringTransactionsPort.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RecurringTransactionNotFoundException("Recurring transaction not found"));

        deleteRecurringTransactionPort.deleteById(id);
    }

    @Override
    public List<RecurringTransactionDetail> execute(Long userId) {
        return loadRecurringTransactionsPort.findAllByUserId(userId);
    }

    @Override
    public RecurringTransactionDetail executeFindByIdAndUserId(Long id, Long userId) {
        return loadRecurringTransactionsPort.findByIdAndUserId(id, userId).orElseThrow(() -> new RecurringTransactionNotFoundException("Recurring transaction not found"));
    }

    @Override
    public RecurringTransaction executeFindById(Long id) {
        return null;
    }

    @Override
    public RecurringTransaction execute(CreateRecurringTransactionCommand command) {
        if (command.getAmount().compareTo(BigDecimal.ZERO) <= 0 || command.getDescription().isEmpty()){
            throw new BadTransactionFieldException("Description cannot be empty and amount must be positive");
        }
        RecurringTransaction recurringTransaction = new RecurringTransaction();
        recurringTransaction.setActive(true);
        recurringTransaction.setLastExecutedAt(null);
        recurringTransaction.setAmount(command.getAmount());
        recurringTransaction.setDescription(command.getDescription());
        recurringTransaction.setType(command.getType());
        recurringTransaction.setDayOfMonth(command.getDayOfMonth());
        recurringTransaction.setUserId(command.getUserId());
        recurringTransaction.setCategoryId(command.getCategoryId());

        return saveRecurringTransactionPort.save(recurringTransaction);
    }

    @Override
    public RecurringTransaction update(UpdateRecurringTransactionCommand command) {
        if (command.getAmount().compareTo(BigDecimal.ZERO) <= 0 || command.getDescription().isEmpty()){
            throw new BadTransactionFieldException("Description cannot be empty and amount must be positive");
        }

        RecurringTransactionDetail foundRecurringTransaction = loadRecurringTransactionsPort.findByIdAndUserId(command.getId(), command.getUserId())
                .orElseThrow(() -> new RecurringTransactionNotFoundException("Recurring transaction not found"));

        RecurringTransaction recurringTransaction = new RecurringTransaction();

        recurringTransaction.setId(command.getId());
        recurringTransaction.setAmount(command.getAmount());
        recurringTransaction.setDescription(command.getDescription());
        recurringTransaction.setType(command.getType());
        recurringTransaction.setDayOfMonth(command.getDayOfMonth());
        recurringTransaction.setUserId(command.getUserId());
        recurringTransaction.setCategoryId(command.getCategoryId());
        recurringTransaction.setLastExecutedAt(command.getLastExecutedAt());
        recurringTransaction.setActive(command.isActive());

        return saveRecurringTransactionPort.save(recurringTransaction);
    }
}
