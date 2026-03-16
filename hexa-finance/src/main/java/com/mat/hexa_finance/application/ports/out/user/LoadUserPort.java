package com.mat.hexa_finance.application.ports.out.user;

import com.mat.hexa_finance.domain.model.User;

import java.util.Optional;

public interface LoadUserPort {
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
}
