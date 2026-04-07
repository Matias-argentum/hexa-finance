package com.mat.hexa_finance.infrastructure.adapters.out.persistence.transaction;

import com.mat.hexa_finance.domain.model.Transaction;
import com.mat.hexa_finance.domain.model.TransactionDetail;
import com.mat.hexa_finance.infrastructure.adapters.out.persistence.category.CategoryEntity;
import com.mat.hexa_finance.infrastructure.adapters.out.persistence.user.UserEntity;

public class TransactionMapper {

    public static Transaction toDomain(TransactionEntity entity){
        Transaction transaction = new Transaction();

        transaction.setId(entity.getId());
        transaction.setAmount(entity.getAmount());
        transaction.setType(entity.getType());
        transaction.setDate(entity.getDate());
        transaction.setDescription(entity.getDescription());
        transaction.setUserId(entity.getUser().getId());
        transaction.setCategoryId(entity.getCategory().getId());

        return transaction;
    }

    public static TransactionEntity toEntity(Transaction transaction, UserEntity userEntity, CategoryEntity categoryEntity){
        TransactionEntity entity = new TransactionEntity();

        entity.setId(transaction.getId());
        entity.setAmount(transaction.getAmount());
        entity.setType(transaction.getType());
        entity.setDate(transaction.getDate());
        entity.setDescription(transaction.getDescription());
        entity.setUser(userEntity);
        entity.setCategory(categoryEntity);

        return entity;
    }

    public static TransactionDetail toDetailDomain(TransactionEntity entity) {
        TransactionDetail detail = new TransactionDetail();
        detail.setId(entity.getId());
        detail.setAmount(entity.getAmount());
        detail.setDescription(entity.getDescription());
        detail.setType(entity.getType());
        detail.setDate(entity.getDate());

        // Aquí no habrá error de Lazy porque usamos JOIN FETCH
        detail.setCategoryName(entity.getCategory().getName());
        detail.setCategoryIcon(entity.getCategory().getIconName());

        return detail;
    }
}
