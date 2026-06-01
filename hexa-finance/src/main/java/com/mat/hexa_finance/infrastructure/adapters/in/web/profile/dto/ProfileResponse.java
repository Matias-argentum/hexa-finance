package com.mat.hexa_finance.infrastructure.adapters.in.web.profile.dto;

import com.mat.hexa_finance.domain.model.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileResponse {
    private Long id;
    private String email;
    private Role role;
    private String username;
}
