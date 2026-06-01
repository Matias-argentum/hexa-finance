package com.mat.hexa_finance.infrastructure.config;

import com.mat.hexa_finance.application.ports.out.user.LoadUserPort;
import com.mat.hexa_finance.application.ports.out.user.SaveUserPort;
import com.mat.hexa_finance.application.service.profile.ProfileService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ProfileBeanConfig {
    @Bean
    ProfileService getProfileservice(LoadUserPort loadUserPort, SaveUserPort saveUserPort, PasswordEncoder passwordEncoder){
        return new ProfileService(loadUserPort, saveUserPort, passwordEncoder);
    }
}
