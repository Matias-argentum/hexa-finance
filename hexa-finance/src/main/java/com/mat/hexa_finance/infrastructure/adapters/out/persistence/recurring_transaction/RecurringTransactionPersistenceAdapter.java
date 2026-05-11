package com.mat.hexa_finance.infrastructure.adapters.out.persistence.recurring_transaction;

import com.mat.hexa_finance.application.ports.out.recurring_transaction.DeleteRecurringTransactionPort;
import com.mat.hexa_finance.application.ports.out.recurring_transaction.LoadRecurringTransactionsPort;
import com.mat.hexa_finance.application.ports.out.recurring_transaction.SaveRecurringTransactionPort;
import com.mat.hexa_finance.domain.exception.CategoryNotFoundException;
import com.mat.hexa_finance.domain.model.RecurringTransaction;
import com.mat.hexa_finance.domain.model.RecurringTransactionDetail;
import com.mat.hexa_finance.infrastructure.adapters.out.persistence.category.CategoryEntity;
import com.mat.hexa_finance.infrastructure.adapters.out.persistence.category.JpaCategoryRepository;
import com.mat.hexa_finance.infrastructure.adapters.out.persistence.user.JpaUserRepository;
import com.mat.hexa_finance.infrastructure.adapters.out.persistence.user.UserEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

public class RecurringTransactionPersistenceAdapter implements
        LoadRecurringTransactionsPort,
        SaveRecurringTransactionPort,
        DeleteRecurringTransactionPort {

    private final JpaRecurringTransactionRepository recurringTransactionRepository;
    private final JpaUserRepository userRepository;
    private final JpaCategoryRepository categoryRepository;

    public RecurringTransactionPersistenceAdapter(JpaRecurringTransactionRepository recurringTransactionRepository, JpaUserRepository userRepository, JpaCategoryRepository categoryRepository) {
        this.recurringTransactionRepository = recurringTransactionRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<RecurringTransactionDetail> findAllByUserId(Long userId) {
        return recurringTransactionRepository.findAllByUserIdWithCategory(userId)
                .stream().map(RecurringTransactionMapper::toDetailedDomain).toList();
    }

    @Override
    public Optional<RecurringTransactionDetail> findByIdAndUserId(Long id, Long userId) {
        Optional<RecurringTransactionEntity> recurringTransactionEntity = recurringTransactionRepository.findByIdAndUserId(id, userId);
        return recurringTransactionEntity.map(RecurringTransactionMapper::toDetailedDomain);
    }

    @Override
    public RecurringTransaction save(RecurringTransaction transaction) {
        CategoryEntity categoryEntity = categoryRepository.findById(transaction.getCategoryId())
                .orElseThrow(()-> new CategoryNotFoundException("Category does not exist"));
        UserEntity userEntity = userRepository.findById(transaction.getUserId())
                .orElseThrow(()-> new UsernameNotFoundException("user does not exist"));

        RecurringTransactionEntity entity = RecurringTransactionMapper.toEntity(transaction, userEntity, categoryEntity);

        RecurringTransactionEntity savedEntity = recurringTransactionRepository.save(entity);
        return RecurringTransactionMapper.toDomain(savedEntity);
    }

    @Override
    public void deleteById(Long id) {
        recurringTransactionRepository.deleteById(id);
    }


}
