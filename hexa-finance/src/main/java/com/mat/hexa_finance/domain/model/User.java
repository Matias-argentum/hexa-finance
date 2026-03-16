package com.mat.hexa_finance.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class User {
    private Long id;
    private String email;
    private String username;
    private String password;
    private Role role;
    private boolean active;
    private boolean forcePasswordChange;
}
