package com.mat.hexa_finance.application.service.recurring_transaction;

import com.mat.hexa_finance.application.ports.out.recurring_transaction.LoadRecurringTransactionsPort;
import com.mat.hexa_finance.application.ports.out.recurring_transaction.SaveRecurringTransactionPort;
import com.mat.hexa_finance.application.ports.out.transaction.SaveTransactionPort;
import com.mat.hexa_finance.domain.model.RecurringTransaction;
import com.mat.hexa_finance.domain.model.Transaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AutomatedRecurringTransactionServiceTest {

    @Mock
    LoadRecurringTransactionsPort loadRecurringTransactionsPort;
    @Mock
    SaveRecurringTransactionPort saveRecurringTransactionPort;
    @Mock
    SaveTransactionPort saveTransactionPort;
    @InjectMocks
    AutomatedRecurringTransactionService automatedRecurringTransactionService;

    @Test
    void shouldProcessRecurringTransactionWhenNeverExecutedBefore(){
        LocalDate today = LocalDate.now();
        int currentDay = today.getDayOfMonth();

        RecurringTransaction rt = new RecurringTransaction();
        rt.setId(1L);
        rt.setUserId(1L);
        rt.setAmount(BigDecimal.valueOf(100));
        rt.setLastExecutedAt(null);

        when(loadRecurringTransactionsPort.findAllByDayOfMonth(currentDay))
                .thenReturn(List.of(rt));

        var captor = ArgumentCaptor.forClass(RecurringTransaction.class);

        automatedRecurringTransactionService.execute();

        verify(saveTransactionPort, times(1)).save(any(Transaction.class));
        verify(saveRecurringTransactionPort, times(1)).save(captor.capture());

        Assertions.assertNotNull(captor.getValue().getLastExecutedAt());
    }

    @Test
    void shouldProcessRecurringTransactionWhenLastExecutedInPreviousMonth(){
        LocalDate today = LocalDate.now();
        int currentDay = today.getDayOfMonth();

        RecurringTransaction rt = new RecurringTransaction();
        rt.setId(1L);
        rt.setUserId(1L);
        rt.setAmount(BigDecimal.valueOf(100));
        rt.setLastExecutedAt(today.minusMonths(1));

        when(loadRecurringTransactionsPort.findAllByDayOfMonth(currentDay))
                .thenReturn(List.of(rt));

        var captor = ArgumentCaptor.forClass(RecurringTransaction.class);

        automatedRecurringTransactionService.execute();

        verify(saveTransactionPort, times(1)).save(any(Transaction.class));
        verify(saveRecurringTransactionPort, times(1)).save(captor.capture());

        Assertions.assertNotNull(captor.getValue().getLastExecutedAt());

    }

    @Test
    void shouldSkipRecurringTransactionWhenAlreadyExecutedInCurrentMonth(){
        LocalDate today = LocalDate.now();
        int currentDay = today.getDayOfMonth();

        RecurringTransaction rt = new RecurringTransaction();
        rt.setId(1L);
        rt.setUserId(1L);
        rt.setAmount(BigDecimal.valueOf(100));
        rt.setLastExecutedAt(today);

        when(loadRecurringTransactionsPort.findAllByDayOfMonth(currentDay))
                .thenReturn(List.of(rt));


        automatedRecurringTransactionService.execute();

        verify(saveTransactionPort, times(0)).save(any(Transaction.class));
        verify(saveRecurringTransactionPort, times(0)).save(any(RecurringTransaction.class));

    }
}
