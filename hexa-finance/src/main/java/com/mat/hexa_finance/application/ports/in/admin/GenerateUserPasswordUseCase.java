package com.mat.hexa_finance.application.ports.in.admin;

import com.mat.hexa_finance.domain.model.User;

public interface GenerateUserPasswordUseCase {
    User execute(Long userId);
}
