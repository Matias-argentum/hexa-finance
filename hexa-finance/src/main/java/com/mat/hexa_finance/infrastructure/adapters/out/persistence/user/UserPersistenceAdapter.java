package com.mat.hexa_finance.infrastructure.adapters.out.persistence.user;

import com.mat.hexa_finance.application.ports.out.user.LoadUserPort;
import com.mat.hexa_finance.application.ports.out.user.SaveUserPort;
import com.mat.hexa_finance.domain.model.User;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserPersistenceAdapter implements LoadUserPort, SaveUserPort {

    private final JpaUserRepository repository;

    public UserPersistenceAdapter(JpaUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<User> findById(Long id) {

        Optional<UserEntity> foundUser = repository.findById(id);
        return foundUser.map(UserMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        Optional<UserEntity> foundUser = repository.findByEmail(email);
        return foundUser.map(UserMapper::toDomain);
    }

    @Override
    public User save(User user) {
        UserEntity entity = UserMapper.toEntity(user);
        return UserMapper.toDomain(repository.save(entity));
    }
}
