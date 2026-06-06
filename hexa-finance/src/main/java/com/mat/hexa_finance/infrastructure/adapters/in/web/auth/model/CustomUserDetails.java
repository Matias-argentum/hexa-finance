package com.mat.hexa_finance.infrastructure.adapters.in.web.auth.model;

import com.mat.hexa_finance.domain.model.Role;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@Setter
public class CustomUserDetails extends User {
    private Long id;
    private String username;
    private String email;
    private Role role;
    private boolean forcePasswordChange;

    public CustomUserDetails(com.mat.hexa_finance.domain.model.User user, Collection<? extends GrantedAuthority> authorities) {
        // constructor completo de Spring Security:
        super(
                user.getEmail(),
                user.getPassword(),
                user.isActive(),  // <--- Si esto es false, Spring Security rebota el login
                true,             // accountNonExpired
                true,             // credentialsNonExpired
                user.isActive(),             // accountNonLocked
                authorities
        );

        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.forcePasswordChange = user.isForcePasswordChange();
    }

    public boolean isAdmin(){
        return this.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}
