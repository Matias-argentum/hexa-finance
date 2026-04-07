package com.mat.hexa_finance.infrastructure.adapters.in.web.transaction.mapper;

import com.mat.hexa_finance.domain.model.TransactionDetail;
import com.mat.hexa_finance.infrastructure.adapters.in.web.transaction.dto.TransactionResponse;

public class TransactionWebMapper {

    public static TransactionResponse toResponse(TransactionDetail transaction){

        String type = transaction.getType().toString().equals("EXPENSE") ? "Gasto" : "Ingreso";

        return new TransactionResponse(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getDescription(),
                type,
                transaction.getDate(),
                transaction.getCategoryName(),
                transaction.getCategoryIcon()
        );
    }
}
