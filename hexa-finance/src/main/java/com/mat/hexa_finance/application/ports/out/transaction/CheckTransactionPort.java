package com.mat.hexa_finance.application.ports.out.transaction;

public interface CheckTransactionPort {
    boolean existsById(Long id);
}
