package com.mat.hexa_finance.infrastructure.adapters.out.persistence.transaction;

import com.mat.hexa_finance.application.ports.out.transaction.CheckTransactionPort;
import com.mat.hexa_finance.application.ports.out.transaction.DeleteTransactionPort;
import com.mat.hexa_finance.application.ports.out.transaction.LoadTransactionsPort;
import com.mat.hexa_finance.application.ports.out.transaction.SaveTransactionPort;
import com.mat.hexa_finance.domain.exception.CategoryNotFoundException;
import com.mat.hexa_finance.domain.exception.TransactionNotFoundException;
import com.mat.hexa_finance.domain.model.Transaction;
import com.mat.hexa_finance.domain.model.TransactionDetail;
import com.mat.hexa_finance.infrastructure.adapters.out.persistence.category.CategoryEntity;
import com.mat.hexa_finance.infrastructure.adapters.out.persistence.category.JpaCategoryRepository;
import com.mat.hexa_finance.infrastructure.adapters.out.persistence.user.JpaUserRepository;
import com.mat.hexa_finance.infrastructure.adapters.out.persistence.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class TransactionPersistenceAdapter implements
        CheckTransactionPort,
        DeleteTransactionPort,
        LoadTransactionsPort,
        SaveTransactionPort {

    private final JpaTransactionRepository transactionRepository;
    private  final JpaUserRepository userRepository;
    private final JpaCategoryRepository categoryRepository;

    public TransactionPersistenceAdapter(JpaTransactionRepository transactionRepository, JpaUserRepository userRepository, JpaCategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public boolean existsById(Long id) {
        return transactionRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        transactionRepository.deleteById(id);
    }

    @Override
    public List<TransactionDetail> findAllByUserId(Long userId) {
        return transactionRepository.findAllByUserIdWithCategory(userId).stream().map(TransactionMapper::toDetailDomain).toList();
    }

    @Override
    public Optional<Transaction> findById(Long id) {
        Optional<TransactionEntity> foundTransaction = transactionRepository.findById(id);

        return foundTransaction.map(TransactionMapper::toDomain);
    }

    @Override
    public Optional<TransactionDetail> findByIdAndUserId(Long id, Long userId) {
        Optional<TransactionEntity> foundTransaction = transactionRepository.findByIdAndUserId(id, userId);
                ;

        return foundTransaction.map(TransactionMapper::toDetailDomain);
    }

    @Override
    public Page<TransactionDetail> findAllByUserIdPaginated(Long userId, LocalDate from, LocalDate to, Pageable pageable) {
        Page<TransactionEntity> page = transactionRepository.findAllByUserIdAndDateBetween(userId, from, to, pageable);
        return page.map(TransactionMapper::toDetailDomain);
    }

    @Override
    public Transaction save(Transaction transaction) {
        CategoryEntity categoryEntity = categoryRepository.findById(transaction.getCategoryId())
                                                        .orElseThrow(()-> new CategoryNotFoundException("Category does not exist"));
        UserEntity userEntity = userRepository.findById(transaction.getUserId())
                .orElseThrow(()-> new UsernameNotFoundException("user does not exist"));

        TransactionEntity entity = TransactionMapper.toEntity(transaction, userEntity, categoryEntity);

        TransactionEntity savedTransaction = transactionRepository.save(entity);
        return TransactionMapper.toDomain(savedTransaction);
    }
}
