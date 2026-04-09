package com.mat.hexa_finance.application.service;

import com.mat.hexa_finance.application.ports.in.transaction.*;
import com.mat.hexa_finance.application.ports.in.transaction.command.CreateTransactionCommand;
import com.mat.hexa_finance.application.ports.in.transaction.command.UpdateTransactionCommand;
import com.mat.hexa_finance.application.ports.out.transaction.CheckTransactionPort;
import com.mat.hexa_finance.application.ports.out.transaction.DeleteTransactionPort;
import com.mat.hexa_finance.application.ports.out.transaction.LoadTransactionsPort;
import com.mat.hexa_finance.application.ports.out.transaction.SaveTransactionPort;
import com.mat.hexa_finance.domain.exception.BadTransactionFieldException;
import com.mat.hexa_finance.domain.exception.TransactionNotFoundException;
import com.mat.hexa_finance.domain.model.Transaction;
import com.mat.hexa_finance.domain.model.TransactionDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class TransactionService implements SaveTransactionUseCase,
        FindAllTransactionsUseCase,
        DeleteTransactionUseCase,
        GetTransactionUseCase,
        UpdateTransactionUseCase {

    private final SaveTransactionPort saveTransactionPort;
    private final DeleteTransactionPort deleteTransactionPort;
    private final LoadTransactionsPort loadTransactionsPort;
    private final CheckTransactionPort checkTransactionPort;

    public TransactionService(SaveTransactionPort saveTransactionPort, DeleteTransactionPort deleteTransactionPort, LoadTransactionsPort loadTransactionsPort, CheckTransactionPort checkTransactionPort) {
        this.saveTransactionPort = saveTransactionPort;
        this.deleteTransactionPort = deleteTransactionPort;
        this.loadTransactionsPort = loadTransactionsPort;
        this.checkTransactionPort = checkTransactionPort;
    }

    @Override
    public void delete(Long id, Long userId) {

        TransactionDetail foundTransaction =  loadTransactionsPort.findByIdAndUserId(id, userId).orElseThrow(()-> new TransactionNotFoundException("Transaction does not exist"));

        deleteTransactionPort.deleteById(id);
    }

    @Override
    public List<TransactionDetail> execute(Long userId) {
        return loadTransactionsPort.findAllByUserId(userId);
    }

    @Override
    public Page<TransactionDetail> executeForPagination(Long userId, LocalDate from, LocalDate to, Pageable pageable) {
        return loadTransactionsPort.findAllByUserIdPaginated(userId, from, to, pageable);
    }

    @Override
    public Transaction executeFindById(Long id) {
        return loadTransactionsPort.findById(id).orElseThrow(()-> new TransactionNotFoundException("Transaction does not exist"));
    }

    @Override
    public TransactionDetail executeFindByIdAndUserId(Long id, Long userId) {
        return loadTransactionsPort.findByIdAndUserId(id, userId).orElseThrow(()-> new TransactionNotFoundException("Transaction does not exist"));
    }

    @Override
    public Transaction execute(CreateTransactionCommand command) {

        if (command.getAmount().compareTo(BigDecimal.ZERO) <= 0 || command.getDescription().isEmpty()){
            throw new BadTransactionFieldException("Description cannot be empty and amount must be positive");
        }

        Transaction transaction = new Transaction();
        transaction.setAmount(command.getAmount());
        transaction.setType(command.getType());
        transaction.setDescription(command.getDescription());
        transaction.setCategoryId(command.getCategoryId());
        transaction.setDate(command.getDate());
        transaction.setUserId(command.getUserId());

        return saveTransactionPort.save(transaction);
    }

    @Override
    public Transaction update(UpdateTransactionCommand command) {

        if (command.getAmount().compareTo(BigDecimal.ZERO) <= 0 || command.getDescription().isEmpty()){
            throw new BadTransactionFieldException("Description cannot be empty and amount must be positive");
        }

        Transaction foundTransaction =  loadTransactionsPort.findById(command.getId()).orElseThrow(()-> new TransactionNotFoundException("Transaction does not exist"));
        if (!foundTransaction.getUserId().equals(command.getUserId())){
            throw new AccessDeniedException("Cannot edit this transaction");
        }

        Transaction transaction = new Transaction();
        transaction.setId(command.getId());
        transaction.setAmount(command.getAmount());
        transaction.setType(command.getType());
        transaction.setDescription(command.getDescription());
        transaction.setCategoryId(command.getCategoryId());
        transaction.setDate(command.getDate());
        transaction.setUserId(command.getUserId());

        return saveTransactionPort.save(transaction);
    }
}
