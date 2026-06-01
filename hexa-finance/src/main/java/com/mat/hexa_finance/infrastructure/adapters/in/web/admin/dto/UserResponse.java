package com.mat.hexa_finance.infrastructure.adapters.in.web.admin.dto;

import com.mat.hexa_finance.domain.model.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {

    private Long id;
    private String email;
    private String username;
    private Role role;
    private boolean active;
    private boolean forcePasswordChange;

}
