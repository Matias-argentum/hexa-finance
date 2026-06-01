package com.mat.hexa_finance.infrastructure.config;

import com.mat.hexa_finance.application.ports.out.admin.LoadAllUsersPort;
import com.mat.hexa_finance.application.ports.out.user.LoadUserPort;
import com.mat.hexa_finance.application.ports.out.user.SaveUserPort;
import com.mat.hexa_finance.application.service.admin.AdminService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminBeanConfig {
    @Bean
    public AdminService adminService(SaveUserPort saveUserPort, LoadAllUsersPort loadAllUsersPort, LoadUserPort loadUserPort, PasswordEncoder passwordEncoder){
        return new AdminService(saveUserPort, loadAllUsersPort, loadUserPort, passwordEncoder);
    }
}
