package com.mat.hexa_finance.application.ports.out.user;

import com.mat.hexa_finance.domain.model.User;

public interface SaveUserPort {
    User save(User user);
}
