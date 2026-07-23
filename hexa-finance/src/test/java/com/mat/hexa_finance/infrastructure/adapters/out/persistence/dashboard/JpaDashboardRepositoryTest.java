package com.mat.hexa_finance.infrastructure.adapters.out.persistence.dashboard;

import com.mat.hexa_finance.domain.model.Role;
import com.mat.hexa_finance.domain.model.TransactionType;
import com.mat.hexa_finance.infrastructure.adapters.out.persistence.category.CategoryEntity;
import com.mat.hexa_finance.infrastructure.adapters.out.persistence.transaction.TransactionEntity;
import com.mat.hexa_finance.infrastructure.adapters.out.persistence.user.UserEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;

@DataJpaTest
public class JpaDashboardRepositoryTest {

    @Autowired
    private JpaDashboardRepository jpaDashboardRepository;
    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void shouldCalculateTotalExpenseAndIncomeByMonthCorrectly(){
        UserEntity user = new UserEntity();
        user.setActive(true);
        user.setRole(Role.USER);
        user.setEmail("user@email.com");
        user.setPassword("1234");
        user.setForcePasswordChange(false);
        user.setUsername("username");

        user = testEntityManager.persistAndFlush(user);

        CategoryEntity cat = new CategoryEntity();
        cat.setName("General");
        cat.setIconName("icon");
        cat = testEntityManager.persistAndFlush(cat);

        LocalDate from = LocalDate.of(2026, 7, 1);
        LocalDate to = LocalDate.of(2026, 7, 31);

        //first transaction
        TransactionEntity income1 = new TransactionEntity();
        income1.setUser(user);
        income1.setType(TransactionType.INCOME);
        income1.setAmount(BigDecimal.valueOf(1000));
        income1.setDate(LocalDate.of(2026, 7, 15));
        income1.setCategory(cat);
        income1.setDescription("Description");
        testEntityManager.persist(income1);

        //second transaction in different month does not count
        TransactionEntity income2out = new TransactionEntity();
        income2out.setUser(user);
        income2out.setType(TransactionType.INCOME);
        income2out.setAmount(BigDecimal.valueOf(500));
        income2out.setDate(LocalDate.of(2026, 6, 20));
        income2out.setCategory(cat);
        income2out.setDescription("Description");
        testEntityManager.persist(income2out);

        // third transaction
        TransactionEntity income3 = new TransactionEntity();
        income3.setUser(user);
        income3.setType(TransactionType.INCOME);
        income3.setAmount(BigDecimal.valueOf(250));
        income3.setDate(LocalDate.of(2026, 7, 16));
        income3.setCategory(cat);
        income3.setDescription("Description");
        testEntityManager.persist(income3);

        //fourth transaction --> expense that counts
        TransactionEntity expense1 = new TransactionEntity();
        expense1.setUser(user);
        expense1.setType(TransactionType.EXPENSE);
        expense1.setAmount(BigDecimal.valueOf(300));
        expense1.setDate(LocalDate.of(2026, 7, 16));
        expense1.setCategory(cat);
        expense1.setDescription("Description");
        testEntityManager.persist(expense1);

        //fifth transaction --> expense that does not count
        TransactionEntity expense2 = new TransactionEntity();
        expense2.setUser(user);
        expense2.setType(TransactionType.EXPENSE);
        expense2.setAmount(BigDecimal.valueOf(250));
        expense2.setDate(LocalDate.of(2026, 4, 16));
        expense2.setCategory(cat);
        expense2.setDescription("Description");
        testEntityManager.persist(expense2);

        testEntityManager.flush();

        BigDecimal totalIncome = jpaDashboardRepository.getTotalMonthlyIncome(user.getId(), from, to);
        BigDecimal totalExpense = jpaDashboardRepository.getTotalMonthlyExpense(user.getId(), from, to);

        Assertions.assertEquals(0, BigDecimal.valueOf(1250).compareTo(totalIncome));
        Assertions.assertEquals(0, BigDecimal.valueOf(300).compareTo(totalExpense));
    }

    @Test
    void shouldReturnNullWhenNoTransactionsExistInRange() {
        UserEntity user = new UserEntity();
        user.setActive(true);
        user.setRole(Role.USER);
        user.setEmail("user@email.com");
        user.setPassword("1234");
        user.setForcePasswordChange(false);
        user.setUsername("username");

        user = testEntityManager.persistAndFlush(user);

        LocalDate from = LocalDate.of(2026, 7, 1);
        LocalDate to = LocalDate.of(2026, 7, 31);

        BigDecimal totalIncome = jpaDashboardRepository.getTotalMonthlyIncome(user.getId(), from, to);
        BigDecimal totalExpense = jpaDashboardRepository.getTotalMonthlyExpense(user.getId(), from, to);

        // La base de datos devuelve null en el SUM cuando no hay registros
        Assertions.assertNull(totalIncome);
        Assertions.assertNull(totalExpense);
    }
}
