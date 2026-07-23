package com.mat.hexa_finance.application.service.profile;

import com.mat.hexa_finance.application.ports.out.user.LoadUserPort;
import com.mat.hexa_finance.application.ports.out.user.SaveUserPort;
import com.mat.hexa_finance.domain.exception.BadUsernameException;
import com.mat.hexa_finance.domain.exception.EmailAlreadyExistsException;
import com.mat.hexa_finance.domain.exception.IncorrectPasswordException;
import com.mat.hexa_finance.domain.exception.UserNotFoundException;
import com.mat.hexa_finance.domain.model.Category;
import com.mat.hexa_finance.domain.model.Role;
import com.mat.hexa_finance.domain.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProfileServiceTest {

    @Mock
    LoadUserPort loadUserPort;
    @Mock
    SaveUserPort saveUserPort;
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    ProfileService profileService;

    @Test
    void shouldChangePasswordAndDisableForcePasswordChangeWhenNewPasswordIsValid() {
        User user = new User();
        user.setPassword("1234");
        user.setEmail("user@email.com");
        user.setForcePasswordChange(true);
        user.setId(1L);
        user.setActive(true);
        user.setRole(Role.USER);
        user.setUsername("user");

        Long userId = 1L;
        String password = "1234";
        String newPassword = "12345678";
        var captor = ArgumentCaptor.forClass(User.class);

        when(loadUserPort.findById(userId))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(user.getPassword(), password)).thenReturn(true);

        when(passwordEncoder.encode(newPassword)).thenReturn("Encrypted");

        when(saveUserPort.save(any(User.class))).thenReturn(user);

        var result = profileService.updatePassword(userId, password, newPassword);
        verify(saveUserPort).save(captor.capture());

        Assertions.assertFalse(captor.getValue().isForcePasswordChange());
        Assertions.assertEquals("Encrypted", captor.getValue().getPassword());

    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {
        User user = new User();
        user.setPassword("1234");
        user.setEmail("user@email.com");
        user.setForcePasswordChange(true);
        user.setId(1L);
        user.setActive(true);
        user.setRole(Role.USER);
        user.setUsername("user");

        Long userId = 2L;
        String password = "1234";
        String newPassword = "12345678";

        when(loadUserPort.findById(userId))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> {
            profileService.updatePassword(userId, password, newPassword);
        });

        verify(saveUserPort, times(0)).save(any(User.class));

    }

    @Test
    void shouldThrowExceptionWhenCurrentPasswordDoesNotMatch() {
        User user = new User();
        user.setPassword("1234");
        user.setEmail("user@email.com");
        user.setForcePasswordChange(true);
        user.setId(1L);
        user.setActive(true);
        user.setRole(Role.USER);
        user.setUsername("user");

        Long userId = 1L;
        String password = "12345"; //si cambio el password me da error de stubbing
        String newPassword = "12345678";

        when(loadUserPort.findById(userId))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(false);

        Assertions.assertThrows(IncorrectPasswordException.class, () -> {
            profileService.updatePassword(userId, password, newPassword);
        });

        verify(saveUserPort, times(0)).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenNewPasswordHasASpace() {
        User user = new User();
        user.setPassword("1234");
        user.setEmail("user@email.com");
        user.setForcePasswordChange(true);
        user.setId(1L);
        user.setActive(true);
        user.setRole(Role.USER);
        user.setUsername("user");

        Long userId = 1L;
        String password = "1234";
        String newPassword = "12345 678";

        when(loadUserPort.findById(userId))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(user.getPassword(), password)).thenReturn(true);

        Assertions.assertThrows(BadUsernameException.class, () -> {
            profileService.updatePassword(userId, password, newPassword);
        });

        verify(saveUserPort, times(0)).save(any(User.class));
    }

    @Test
    void shouldNotUpdateEmailWhenNewEmailAlreadyExists(){
        User user = new User();
        user.setPassword("1234");
        user.setEmail("new@email.com");
        user.setForcePasswordChange(true);
        user.setId(1L);
        user.setActive(true);
        user.setRole(Role.USER);
        user.setUsername("user");

        Long userId = 1L;
        String password = "1234";
        String newEmail = "new@email.com";

        when(loadUserPort.findById(userId)).thenReturn(Optional.of(user));

        when(loadUserPort.findByEmail(newEmail))
                .thenReturn(Optional.of(user));

        Assertions.assertThrows(EmailAlreadyExistsException.class, ()->{
            profileService.updateEmail(userId, password, newEmail);
        });

        verify(saveUserPort, times(0)).save(any(User.class));
    }
}
