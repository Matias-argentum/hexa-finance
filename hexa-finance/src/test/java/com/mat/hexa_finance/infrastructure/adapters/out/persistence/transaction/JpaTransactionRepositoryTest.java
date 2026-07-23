package com.mat.hexa_finance.infrastructure.adapters.out.persistence.transaction;

import com.mat.hexa_finance.domain.model.Role;
import com.mat.hexa_finance.domain.model.TransactionType;
import com.mat.hexa_finance.infrastructure.adapters.out.persistence.category.CategoryEntity;
import com.mat.hexa_finance.infrastructure.adapters.out.persistence.report.CategoryReportProjection;
import com.mat.hexa_finance.infrastructure.adapters.out.persistence.user.UserEntity;
import jakarta.validation.constraints.AssertTrue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@DataJpaTest
public class JpaTransactionRepositoryTest {
    @Autowired
    private JpaTransactionRepository jpaTransactionRepository;
    @Autowired
    private TestEntityManager testEntityManager;

    private UserEntity user;
    private CategoryEntity cat;
    private TransactionEntity income1;
    private TransactionEntity income2;
    private TransactionEntity income3;
    private TransactionEntity expense1;
    private TransactionEntity expense2;

    @BeforeEach
    public  void setUp(){
        user = new UserEntity();
        user.setActive(true);
        user.setRole(Role.USER);
        user.setEmail("user@email.com");
        user.setPassword("1234");
        user.setForcePasswordChange(false);
        user.setUsername("username");
        user = testEntityManager.persistAndFlush(user);

        cat = new CategoryEntity();
        cat.setIconName("icon");
        cat.setName("General");
        cat = testEntityManager.persistAndFlush(cat);

        //income 1 july
        income1 = new TransactionEntity();
        income1.setUser(user);
        income1.setType(TransactionType.INCOME);
        income1.setAmount(BigDecimal.valueOf(1000));
        income1.setDate(LocalDate.of(2026, 7, 15));
        income1.setCategory(cat);
        income1.setDescription("Description");
        testEntityManager.persist(income1);

        //income 2 june
        income2 = new TransactionEntity();
        income2.setUser(user);
        income2.setType(TransactionType.INCOME);
        income2.setAmount(BigDecimal.valueOf(500));
        income2.setDate(LocalDate.of(2026, 6, 20));
        income2.setCategory(cat);
        income2.setDescription("Description");
        testEntityManager.persist(income2);

        // income 3 july
        income3 = new TransactionEntity();
        income3.setUser(user);
        income3.setType(TransactionType.INCOME);
        income3.setAmount(BigDecimal.valueOf(250));
        income3.setDate(LocalDate.of(2026, 7, 16));
        income3.setCategory(cat);
        income3.setDescription("Description");
        testEntityManager.persist(income3);

        //expense 1 july
        expense1 = new TransactionEntity();
        expense1.setUser(user);
        expense1.setType(TransactionType.EXPENSE);
        expense1.setAmount(BigDecimal.valueOf(300));
        expense1.setDate(LocalDate.of(2026, 7, 14));
        expense1.setCategory(cat);
        expense1.setDescription("Description");
        testEntityManager.persist(expense1);

        // expense 2 april
        expense2 = new TransactionEntity();
        expense2.setUser(user);
        expense2.setType(TransactionType.EXPENSE);
        expense2.setAmount(BigDecimal.valueOf(250));
        expense2.setDate(LocalDate.of(2026, 4, 16));
        expense2.setCategory(cat);
        expense2.setDescription("Description");
        testEntityManager.persist(expense2);

        testEntityManager.flush();
    }

    @Test
    void shouldCalculateTotalAmountGroupedByCategoryWhenTransactionsExist(){
        LocalDate from = LocalDate.of(2026, 7, 1);
        LocalDate to = LocalDate.of(2026, 7, 31);
        var expectedTotal = 1250;

        List<CategoryReportProjection> results = jpaTransactionRepository.findReport(user.getId(), from, to, TransactionType.INCOME, List.of(cat.getId()));

        Assertions.assertEquals(cat.getName(), results.getFirst().categoryName());
        Assertions.assertEquals(0, (income1.getAmount().add(income3.getAmount())).compareTo(BigDecimal.valueOf(expectedTotal)));
    }

    @Test
    void shouldReturnZeroForCategoriesWithoutTransactionsInRange(){
        LocalDate from = LocalDate.of(2024, 7, 1);
        LocalDate to = LocalDate.of(2024, 7, 31);
        var expectedTotal = 0;

        List<CategoryReportProjection> results = jpaTransactionRepository.findReport(user.getId(), from, to, TransactionType.INCOME, List.of(cat.getId()));

        Assertions.assertEquals(cat.getName(), results.getFirst().categoryName());
        Assertions.assertEquals(0, results.getFirst().totalAmount().compareTo(BigDecimal.valueOf(expectedTotal)));
    }

    @Test
    void shouldFetchPagedTransactionsFilteredByDateRange(){
        LocalDate from = LocalDate.of(2026, 6, 1);
        LocalDate to = LocalDate.of(2026, 7, 31);

        Page<TransactionEntity> transactions = jpaTransactionRepository.findAllByUserIdAndDateBetween(user.getId(), from, to, Pageable.ofSize(3));

        Assertions.assertNotNull(transactions);
        Assertions.assertEquals(16, transactions.getContent().getFirst().getDate().getDayOfMonth());
        Assertions.assertEquals(14, transactions.getContent().getLast().getDate().getDayOfMonth());

    }

    @Test
    void shouldFindTransactionByIdAndUserIdWithCategory(){
        Long transactionId = income1.getId();
        Long userId = user.getId();

        Optional<TransactionEntity> result = jpaTransactionRepository.findByIdAndUserId(transactionId, userId);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(0, result.get().getAmount().compareTo(BigDecimal.valueOf(1000)));
    }
}
