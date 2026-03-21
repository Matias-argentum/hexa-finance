package com.mat.hexa_finance.infrastructure.adapters.out.persistence.user;

import com.mat.hexa_finance.application.ports.out.user.LoadUserPort;
import com.mat.hexa_finance.application.ports.out.user.SaveUserPort;
import com.mat.hexa_finance.domain.model.Role;
import com.mat.hexa_finance.domain.model.User;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserDataInitializer {
    private final SaveUserPort saveUserPort;
    private final LoadUserPort loadUserPort;
    private final PasswordEncoder passwordEncoder;


    public UserDataInitializer(SaveUserPort saveUserPort, LoadUserPort loadUserPort, PasswordEncoder passwordEncoder) {
        this.saveUserPort = saveUserPort;
        this.loadUserPort = loadUserPort;
        this.passwordEncoder = passwordEncoder;
    }

    @EventListener(ApplicationReadyEvent.class)
    private void insertAdmin(){
        if (loadUserPort.findByEmail("admin@admin.com").isEmpty()){
            User adminUser = new User();
            adminUser.setUsername("Admin");
            adminUser.setRole(Role.ADMIN);
            adminUser.setForcePasswordChange(false);
            adminUser.setActive(true);
            adminUser.setEmail("admin@admin.com");
            adminUser.setPassword(passwordEncoder.encode("1234"));

            saveUserPort.save(adminUser);
            System.out.println("Admin added");
        }
    }


}
