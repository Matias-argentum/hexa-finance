package com.mat.hexa_finance.application.ports.in.profile;

import com.mat.hexa_finance.domain.model.User;

public interface UpdateEmailUseCase {
    User updateEmail(Long userId, String password, String newEmail);
}
