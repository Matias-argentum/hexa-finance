package com.mat.hexa_finance.infrastructure.adapters.in.web.profile.mapper;

import com.mat.hexa_finance.infrastructure.adapters.in.web.auth.model.CustomUserDetails;
import com.mat.hexa_finance.infrastructure.adapters.in.web.profile.dto.ProfileResponse;

public class ProfileWebMapper {
    public static ProfileResponse toResponse(CustomUserDetails userDetails){
        ProfileResponse response = new ProfileResponse();
        response.setId(userDetails.getId());
        response.setRole(userDetails.getRole());
        response.setUsername(userDetails.getUsername());
        response.setEmail(userDetails.getEmail());

        return response;
    }
}
