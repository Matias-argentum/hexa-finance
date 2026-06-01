package com.mat.hexa_finance.application.ports.in.admin;

import com.mat.hexa_finance.domain.model.User;

import java.util.List;

public interface FindAllUsersUseCase {
    List<User> execute();
}
