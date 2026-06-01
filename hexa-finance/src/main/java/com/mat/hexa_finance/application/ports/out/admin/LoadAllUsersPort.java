package com.mat.hexa_finance.application.ports.out.admin;

import com.mat.hexa_finance.domain.model.User;

import java.util.List;

public interface LoadAllUsersPort {
    List<User> findAll();
}
