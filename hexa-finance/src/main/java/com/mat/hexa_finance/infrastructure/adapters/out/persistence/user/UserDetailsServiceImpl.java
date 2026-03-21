package com.mat.hexa_finance.infrastructure.adapters.out.persistence.user;

import com.mat.hexa_finance.application.ports.out.user.LoadUserPort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final LoadUserPort loadUserPort;

    public UserDetailsServiceImpl(LoadUserPort loadUserport) {
        this.loadUserPort = loadUserport;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        com.mat.hexa_finance.domain.model.User user = loadUserPort.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No se encontró el email: " + email));

        // hay que poner los paquetes asi poruqe las dos clses se llaman user
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .disabled(!user.isActive())
                .roles(user.getRole().name()) // Esto añade "ROLE_" automáticamente
                .build();
    }
}
