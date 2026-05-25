package com.mat.hexa_finance.application.service.recurring_transaction;

import com.mat.hexa_finance.application.ports.in.recurring_transaction.ProcessRecurringTransactionUserCase;
import com.mat.hexa_finance.application.ports.out.recurring_transaction.LoadRecurringTransactionsPort;
import com.mat.hexa_finance.application.ports.out.recurring_transaction.SaveRecurringTransactionPort;
import com.mat.hexa_finance.application.ports.out.transaction.SaveTransactionPort;
import com.mat.hexa_finance.domain.model.RecurringTransaction;
import com.mat.hexa_finance.domain.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public class AutomatedRecurringTransactionService implements ProcessRecurringTransactionUserCase {

    private static final Logger log = LoggerFactory.getLogger(AutomatedRecurringTransactionService.class);

    private final LoadRecurringTransactionsPort loadRecurringTransactionsPort;
    private final SaveRecurringTransactionPort saveRecurringTransactionPort;
    private final SaveTransactionPort saveTransactionPort;

    public AutomatedRecurringTransactionService(LoadRecurringTransactionsPort loadRecurringTransactionsPort,
                                                SaveRecurringTransactionPort saveRecurringTransactionPort,
                                                SaveTransactionPort saveTransactionPort) {
        this.loadRecurringTransactionsPort = loadRecurringTransactionsPort;
        this.saveRecurringTransactionPort = saveRecurringTransactionPort;
        this.saveTransactionPort = saveTransactionPort;
    }

    @Override
    @Transactional
    public void execute() {
        LocalDate today = LocalDate.now();
        int day = today.getDayOfMonth();

        log.info("=== INICIANDO CRON DE TRANSACCIONES RECURRENTES ===");
        log.info("Fecha actual: {} | Día del mes a evaluar: {}", today, day);

        List<RecurringTransaction> recurringTransactionsList = loadRecurringTransactionsPort.findAllByDayOfMonth(day);
        log.info("Se encontraron {} transacciones recurrentes para el día {}", recurringTransactionsList.size(), day);

        LocalDate firstDayOfCurrentMonth = today.withDayOfMonth(1);

        recurringTransactionsList.forEach((rt) -> {
            log.info("[RT ID: {}] Evaluando... Descripción: '{}' | LastExecutedAt actual: {}",
                    rt.getId(), rt.getDescription(), rt.getLastExecutedAt());

            // 2. Condición corregida con isBefore
            if (rt.getLastExecutedAt() == null || rt.getLastExecutedAt().isBefore(firstDayOfCurrentMonth)) {
                log.info("[RT ID: {}] -> PASÓ EL FILTRO. Procediendo a crear transacción real.", rt.getId());

                Transaction t = new Transaction();
                t.setUserId(rt.getUserId());
                t.setAmount(rt.getAmount());
                t.setCategoryId(rt.getCategoryId());
                t.setDescription(rt.getDescription());
                t.setDate(today);
                t.setType(rt.getType());

                saveTransactionPort.save(t);
                log.info("[RT ID: {}] -> Transacción real guardada con éxito.", rt.getId());

                rt.setLastExecutedAt(today);
                saveRecurringTransactionPort.save(rt);
                log.info("[RT ID: {}] -> Transacción recurrente actualizada con lastExecutedAt = {}", rt.getId(), today);
            } else {
                log.info("[RT ID: {}] -> OMITIDA. Ya fue ejecutada en este período (el o después del {}).",
                        rt.getId(), firstDayOfCurrentMonth);
            }
        });

        log.info("=== FIN DEL CRON DE TRANSACCIONES RECURRENTES ===");
    }
}