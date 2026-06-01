package com.mat.hexa_finance.application.service.profile;

import com.mat.hexa_finance.application.ports.in.profile.UpdateEmailUseCase;
import com.mat.hexa_finance.application.ports.in.profile.UpdatePasswordUseCase;
import com.mat.hexa_finance.application.ports.in.profile.UpdateUsernameUseCase;
import com.mat.hexa_finance.application.ports.out.user.LoadUserPort;
import com.mat.hexa_finance.application.ports.out.user.SaveUserPort;
import com.mat.hexa_finance.domain.exception.BadUsernameException;
import com.mat.hexa_finance.domain.exception.EmailAlreadyExistsException;
import com.mat.hexa_finance.domain.exception.IncorrectPasswordException;
import com.mat.hexa_finance.domain.exception.UserNotFoundException;
import com.mat.hexa_finance.domain.model.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class ProfileService implements UpdateUsernameUseCase, UpdateEmailUseCase, UpdatePasswordUseCase {
    private final LoadUserPort loadUserPort;
    private final SaveUserPort saveUserPort;
    private final PasswordEncoder passwordEncoder;

    public ProfileService(LoadUserPort loadUserPort, SaveUserPort saveUserPort, PasswordEncoder passwordEncoder) {
        this.loadUserPort = loadUserPort;
        this.saveUserPort = saveUserPort;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User updateEmail(Long userId, String password, String newEmail) {

        User foundUser = loadUserPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (loadUserPort.findByEmail(newEmail).isPresent()) {
            throw new EmailAlreadyExistsException("Email already registered");
        }

        if (!passwordEncoder.matches(password, foundUser.getPassword())) {
            throw new IncorrectPasswordException("Incorrect password");
        }

        if (!newEmail.contains("@")) {
            throw new BadUsernameException("Bad Email");
        }

        foundUser.setEmail(newEmail);

        return saveUserPort.save(foundUser);
    }

    @Override
    public User updatePassword(Long userId, String password, String newPassword) {
        User foundUser = loadUserPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(password, foundUser.getPassword())) {
            throw new IncorrectPasswordException("Incorrect password");
        }

        if (newPassword.isEmpty() || newPassword.contains(" ")) {
            throw new BadUsernameException("Bad username");
        }

        foundUser.setPassword(passwordEncoder.encode(newPassword));

        return saveUserPort.save(foundUser);

    }

    @Override
    public User updateUserName(Long userId, String password, String newUsername) {
        User foundUser = loadUserPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(password, foundUser.getPassword())) {
            throw new IncorrectPasswordException("Incorrect password");
        }

        if (newUsername.isEmpty() || newUsername.contains(" ")) {
            throw new BadUsernameException("Bad username");
        }

        foundUser.setUsername(newUsername);

        return saveUserPort.save(foundUser);
    }
}
