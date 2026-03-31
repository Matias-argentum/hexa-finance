package com.mat.hexa_finance.application.ports.in.transaction;

import com.mat.hexa_finance.application.ports.in.transaction.command.CreateTransactionCommand;
import com.mat.hexa_finance.domain.model.Transaction;

public interface SaveTransactionUseCase {
    Transaction execute(CreateTransactionCommand command);
}
