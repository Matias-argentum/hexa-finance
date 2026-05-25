package com.mat.hexa_finance.infrastructure.adapters.out.persistence.recurring_transaction;

import com.mat.hexa_finance.domain.model.RecurringTransaction;
import com.mat.hexa_finance.domain.model.RecurringTransactionDetail;
import com.mat.hexa_finance.infrastructure.adapters.out.persistence.category.CategoryEntity;
import com.mat.hexa_finance.infrastructure.adapters.out.persistence.user.UserEntity;

public class RecurringTransactionMapper {
    public static RecurringTransaction toDomain(RecurringTransactionEntity entity) {
        RecurringTransaction recurringTransaction = new RecurringTransaction();
        recurringTransaction.setId(entity.getId());
        recurringTransaction.setAmount(entity.getAmount());
        recurringTransaction.setType(entity.getType());
        recurringTransaction.setCategoryId(entity.getCategory().getId());
        recurringTransaction.setDescription(entity.getDescription());
        recurringTransaction.setUserId(entity.getUser().getId());
        recurringTransaction.setDayOfMonth(entity.getDayOfMonth());
        recurringTransaction.setActive(entity.isActive());
        recurringTransaction.setLastExecutedAt(entity.getLastExecutedAt());

        return recurringTransaction;
    }

    public static RecurringTransactionEntity toEntity(RecurringTransaction recurringTransaction, UserEntity userEntity, CategoryEntity categoryEntity) {
        RecurringTransactionEntity entity = new RecurringTransactionEntity();

        entity.setId(recurringTransaction.getId());
        entity.setActive(recurringTransaction.isActive());
        entity.setAmount(recurringTransaction.getAmount());
        entity.setCategory(categoryEntity);
        entity.setDescription(recurringTransaction.getDescription());
        entity.setDayOfMonth(recurringTransaction.getDayOfMonth());
        entity.setLastExecutedAt(recurringTransaction.getLastExecutedAt());
        entity.setUser(userEntity);
        entity.setType(recurringTransaction.getType());

        return entity;
    }

    public static RecurringTransactionDetail toDetailedDomain(RecurringTransactionEntity entity) {
        RecurringTransactionDetail recurringTransactionDetail = new RecurringTransactionDetail();
        recurringTransactionDetail.setId(entity.getId());
        recurringTransactionDetail.setActive(entity.isActive());
        recurringTransactionDetail.setAmount(entity.getAmount());
        recurringTransactionDetail.setDayOfMonth(entity.getDayOfMonth());
        recurringTransactionDetail.setDescription(entity.getDescription());
        recurringTransactionDetail.setCategoryIcon(entity.getCategory().getIconName());
        recurringTransactionDetail.setCategoryName(entity.getCategory().getName());
        recurringTransactionDetail.setLastExecutedAt(entity.getLastExecutedAt());
        recurringTransactionDetail.setUserId(entity.getUser().getId());
        recurringTransactionDetail.setType(entity.getType());
        recurringTransactionDetail.setLastExecutedAt(entity.getLastExecutedAt());

        return recurringTransactionDetail;
    }
}
