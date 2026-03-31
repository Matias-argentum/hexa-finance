package com.mat.hexa_finance.application.ports.in.transaction;

import com.mat.hexa_finance.application.ports.in.transaction.command.UpdateTransactionCommand;
import com.mat.hexa_finance.domain.model.Transaction;

public interface UpdateTransactionUseCase {
    Transaction update(UpdateTransactionCommand command);
}
