package com.mat.hexa_finance.application.service.user;

import com.mat.hexa_finance.application.ports.in.user.RegisterUserUseCase;
import com.mat.hexa_finance.application.ports.in.user.command.RegisterUserCommand;
import com.mat.hexa_finance.application.ports.out.user.LoadUserPort;
import com.mat.hexa_finance.application.ports.out.user.SaveUserPort;
import com.mat.hexa_finance.domain.exception.EmailAlreadyExistsException;
import com.mat.hexa_finance.domain.model.Role;
import com.mat.hexa_finance.domain.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserService implements RegisterUserUseCase {

    private final SaveUserPort saveUserPort;
    private final LoadUserPort loadUserPort;
    private final PasswordEncoder passwordEncoder;

    public UserService(SaveUserPort saveUserPort, LoadUserPort loadUserPort, PasswordEncoder passwordEncoder) {
        this.saveUserPort = saveUserPort;
        this.loadUserPort = loadUserPort;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User execute(RegisterUserCommand command) {
        if(loadUserPort.findByEmail(command.email()).isPresent()){
            throw new EmailAlreadyExistsException("Email already registered");
        }
        User newUser = new User();
        newUser.setEmail(command.email());
        newUser.setUsername(command.username());
        newUser.setPassword(passwordEncoder.encode(command.password()));
        newUser.setRole(Role.USER);
        newUser.setActive(true);
        newUser.setForcePasswordChange(false);

        return saveUserPort.save(newUser);
    }
}
