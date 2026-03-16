package com.mat.hexa_finance.infrastructure.adapters.out.persistence.user;

import com.mat.hexa_finance.domain.model.User;
import jakarta.persistence.Entity;

public class UserMapper {
    public static User toDomain(UserEntity entity){
        User user = new User();
        user.setId(entity.getId());
        user.setEmail(entity.getEmail());
        user.setUsername(entity.getUsername());
        user.setPassword(entity.getPassword());
        user.setRole(entity.getRole());
        user.setActive(entity.isActive());
        user.setForcePasswordChange(entity.isForcePasswordChange());

        return user;
    }

    public static UserEntity toEntity(User user){
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setEmail(user.getEmail());
        entity.setUsername(user.getUsername());
        entity.setPassword(user.getPassword());
        entity.setRole(user.getRole());
        entity.setActive(user.isActive());
        entity.setForcePasswordChange(user.isForcePasswordChange());

        return entity;
    }
}
