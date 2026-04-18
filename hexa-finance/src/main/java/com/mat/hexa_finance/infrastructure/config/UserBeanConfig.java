package com.mat.hexa_finance.infrastructure.config;

import com.mat.hexa_finance.application.ports.out.user.LoadUserPort;
import com.mat.hexa_finance.application.ports.out.user.SaveUserPort;
import com.mat.hexa_finance.application.service.user.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserBeanConfig {

    @Bean
    public UserService userService(SaveUserPort saveUserPort, LoadUserPort loadUserPort, PasswordEncoder passwordEncoder){
        return new UserService(saveUserPort, loadUserPort, passwordEncoder);
    }
}
