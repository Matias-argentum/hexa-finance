package com.mat.hexa_finance.application.ports.in.user;

import com.mat.hexa_finance.application.ports.in.user.command.RegisterUserCommand;
import com.mat.hexa_finance.domain.model.User;

public interface RegisterUserUseCase {
    User execute(RegisterUserCommand command);
}
