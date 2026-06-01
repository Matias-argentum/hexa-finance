package com.mat.hexa_finance.application.service.admin;

import com.mat.hexa_finance.application.ports.in.admin.FindAllUsersUseCase;
import com.mat.hexa_finance.application.ports.in.admin.GenerateUserPasswordUseCase;
import com.mat.hexa_finance.application.ports.in.admin.ToggleUserStatusUseCase;
import com.mat.hexa_finance.application.ports.out.admin.LoadAllUsersPort;
import com.mat.hexa_finance.application.ports.out.user.LoadUserPort;
import com.mat.hexa_finance.application.ports.out.user.SaveUserPort;
import com.mat.hexa_finance.domain.model.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;

public class AdminService implements FindAllUsersUseCase, GenerateUserPasswordUseCase, ToggleUserStatusUseCase {
    private final SaveUserPort saveUserPort;
    private final LoadAllUsersPort loadAllUsersPort;
    private final LoadUserPort loadUserPort;
    private final PasswordEncoder passwordEncoder;

    public AdminService(SaveUserPort saveUserPort, LoadAllUsersPort loadAllUsersPort, LoadUserPort loadUserPort, PasswordEncoder passwordEncoder) {
        this.saveUserPort = saveUserPort;
        this.loadAllUsersPort = loadAllUsersPort;
        this.loadUserPort = loadUserPort;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> execute() {
        return loadAllUsersPort.findAll();
    }

    @Override
    public User execute(Long userId) {
        User foundUser = loadUserPort.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String newPassword = "temp" + foundUser.getUsername() + LocalDate.now().getDayOfMonth();
        foundUser.setPassword(passwordEncoder.encode(newPassword));
        foundUser.setForcePasswordChange(true);
        return saveUserPort.save(foundUser);
    }

    @Override
    public User executeToggle(Long userId) {
        User foundUser = loadUserPort.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        foundUser.setActive(!foundUser.isActive());
        return saveUserPort.save(foundUser);
    }
}
