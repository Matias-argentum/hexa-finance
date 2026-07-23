package com.mat.hexa_finance.application.service.transaction;

import com.mat.hexa_finance.application.ports.in.transaction.command.CreateTransactionCommand;
import com.mat.hexa_finance.application.ports.out.transaction.CheckTransactionPort;
import com.mat.hexa_finance.application.ports.out.transaction.DeleteTransactionPort;
import com.mat.hexa_finance.application.ports.out.transaction.LoadTransactionsPort;
import com.mat.hexa_finance.application.ports.out.transaction.SaveTransactionPort;
import com.mat.hexa_finance.domain.exception.BadTransactionFieldException;
import com.mat.hexa_finance.domain.model.Transaction;
import com.mat.hexa_finance.domain.model.TransactionDetail;
import com.mat.hexa_finance.domain.model.TransactionType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    SaveTransactionPort saveTransactionPort;
    @Mock
    DeleteTransactionPort deleteTransactionPort;
    @Mock
    LoadTransactionsPort loadTransactionsPort;
    @Mock
    CheckTransactionPort checkTransactionPort;

    @InjectMocks
    TransactionService transactionService;

    @Test
    void shouldReturnPaginatedTransactionsSuccessfully(){
        TransactionDetail t1 = new TransactionDetail();
        t1.setId(1L);
        t1.setAmount(BigDecimal.valueOf(500));
        t1.setDate(LocalDate.of(2026, 6, 20));
        t1.setCategoryName("Category name");
        t1.setDescription("Description 1");
        t1.setType(TransactionType.EXPENSE);
        t1.setCategoryIcon("icon");

        TransactionDetail t2 = new TransactionDetail();
        t2.setId(2L);
        t2.setAmount(BigDecimal.valueOf(600));
        t2.setDate(LocalDate.of(2026, 6, 2));
        t2.setCategoryName("Category name 2");
        t2.setDescription("Description 2");
        t2.setType(TransactionType.EXPENSE);
        t2.setCategoryIcon("icon");

        TransactionDetail t3 = new TransactionDetail();
        t3.setId(3L);
        t3.setAmount(BigDecimal.valueOf(600));
        t3.setDate(LocalDate.of(2026, 6, 1));
        t3.setCategoryName("Category name 2");
        t3.setDescription("Description 2");
        t3.setType(TransactionType.EXPENSE);
        t3.setCategoryIcon("icon");

        Long userId = 1L;
        LocalDate from = LocalDate.of(2026, 5, 1);
        LocalDate to = LocalDate.of(2026, 7, 1);

        Pageable pageable = PageRequest.of(0, 1); // page=0, size=1

        Page<TransactionDetail> detailedList =
                new PageImpl<>(List.of(t1), pageable, 1L); // totalElements = 1

        when(loadTransactionsPort.findAllByUserIdPaginated(userId, from, to, pageable))
                .thenReturn(detailedList);

        Page<TransactionDetail> result = transactionService.executeForPagination(userId, from, to, pageable);


        Assertions.assertEquals(1, result.getTotalElements());
        Assertions.assertEquals(1L, result.getContent().getFirst().getId());

        verify(loadTransactionsPort).findAllByUserIdPaginated(userId, from, to, pageable);

    }

    @Test
    void shouldSaveTransactionSuccessfully(){

        var command = new CreateTransactionCommand();
        command.setAmount(BigDecimal.valueOf(500));
        command.setUserId(1L);
        command.setDescription("Description");
        command.setType(TransactionType.EXPENSE);
        command.setDate(LocalDate.now());
        command.setCategoryId(1L);

        var created = new Transaction();
        created.setType(TransactionType.EXPENSE);
        created.setUserId(1L);
        created.setAmount(BigDecimal.valueOf(500));
        created.setCategoryId(1L);
        created.setDate(command.getDate());
        created.setDescription("Description");
        created.setId(1L);

        var captor = ArgumentCaptor.forClass(Transaction.class);

        when(saveTransactionPort.save(any(Transaction.class))).thenReturn(created);

        var result = transactionService.execute(command);

        verify(saveTransactionPort).save(captor.capture());
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());

        Assertions.assertEquals("Description", captor.getValue().getDescription());
        Assertions.assertEquals(1L, captor.getValue().getUserId());
    }

    @Test
    void shouldThrowExceptionWhenAmountIsLessThanOrEqualToZero(){
        var command = new CreateTransactionCommand();
        command.setAmount(BigDecimal.valueOf(-500));
        command.setUserId(1L);
        command.setDescription("Description");
        command.setType(TransactionType.EXPENSE);
        command.setDate(LocalDate.now());
        command.setCategoryId(1L);

        Assertions.assertThrows(BadTransactionFieldException.class, ()-> {
            transactionService.execute(command);
        });

        verify(saveTransactionPort, times(0)).save(any(Transaction.class));

    }
}
