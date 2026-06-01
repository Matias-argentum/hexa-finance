package com.mat.hexa_finance.application.ports.in.admin;

import com.mat.hexa_finance.domain.model.User;

public interface ToggleUserStatusUseCase {
    User executeToggle(Long userId);
}
