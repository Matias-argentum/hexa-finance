package com.mat.hexa_finance.infrastructure.adapters.in.web.admin.mapper;

import com.mat.hexa_finance.domain.model.User;
import com.mat.hexa_finance.infrastructure.adapters.in.web.admin.dto.UserResponse;

public class UserWebMapper {
    public static UserResponse toResponse(User user){
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setRole(user.getRole());
        response.setActive(user.isActive());
        response.setEmail(user.getEmail());
        response.setUsername(user.getUsername());
        response.setForcePasswordChange(user.isForcePasswordChange());

        return response;
    }
}
