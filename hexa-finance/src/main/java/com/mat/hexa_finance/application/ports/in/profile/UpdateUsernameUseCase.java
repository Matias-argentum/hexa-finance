package com.mat.hexa_finance.application.ports.in.profile;

import com.mat.hexa_finance.domain.model.User;

public interface UpdateUsernameUseCase {
    User updateUserName(Long userId, String password, String newUsername);
}
