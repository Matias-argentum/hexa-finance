package com.mat.hexa_finance.application.ports.in.profile;

import com.mat.hexa_finance.domain.model.User;

public interface UpdatePasswordUseCase {
    User updatePassword(Long userId, String password, String newPassword);
}
