package com.mat.hexa_finance.infrastructure.adapters.in.web.admin;

import com.mat.hexa_finance.application.ports.in.admin.FindAllUsersUseCase;
import com.mat.hexa_finance.application.ports.in.admin.GenerateUserPasswordUseCase;
import com.mat.hexa_finance.application.ports.in.admin.ToggleUserStatusUseCase;
import com.mat.hexa_finance.domain.model.User;
import com.mat.hexa_finance.infrastructure.adapters.in.web.admin.dto.UserResponse;
import com.mat.hexa_finance.infrastructure.adapters.in.web.admin.mapper.UserWebMapper;
import com.mat.hexa_finance.infrastructure.adapters.in.web.auth.model.CustomUserDetails;
import com.mat.hexa_finance.infrastructure.adapters.in.web.common.utils.HtmxUtils.HtmxUtils;
import com.mat.hexa_finance.infrastructure.adapters.out.persistence.user.UserMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final FindAllUsersUseCase findAllUsersUseCase;
    private final GenerateUserPasswordUseCase generateUserPasswordUseCase;
    private final ToggleUserStatusUseCase toggleUserStatusUseCase;

    public AdminController(FindAllUsersUseCase findAllUsersUseCase, GenerateUserPasswordUseCase generateUserPasswordUseCase, ToggleUserStatusUseCase toggleUserStatusUseCase) {
        this.findAllUsersUseCase = findAllUsersUseCase;
        this.generateUserPasswordUseCase = generateUserPasswordUseCase;
        this.toggleUserStatusUseCase = toggleUserStatusUseCase;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public String getUsers(Model model) {

        List<UserResponse> users = findAllUsersUseCase.execute().stream().map(UserWebMapper::toResponse).toList();
        model.addAttribute("users", users);

        model.addAttribute("content", "admin/usersList");
        return "layouts/main-layout";

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/toggle")
    public String toggleStatus(@PathVariable("id") Long userId,  Model model) {

       UserResponse user = UserWebMapper.toResponse(toggleUserStatusUseCase.executeToggle(userId));
        model.addAttribute("user", user);

        return "admin/fragments/admin-fragments :: user-row(user=${user})";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/reset-password")
    public String forceRsetPassword(@PathVariable("id") Long userId, HttpServletResponse response, Model model) {

        User user = generateUserPasswordUseCase.execute(userId);
        UserResponse userResponse = UserWebMapper.toResponse(user);
        String message = "Password nuevo generado para: " + user.getEmail();

        HtmxUtils.addNotification(response, message);
        model.addAttribute("user", userResponse);

        return "admin/fragments/admin-fragments :: user-row(user=${user})";
    }
}
