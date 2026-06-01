package com.mat.hexa_finance.infrastructure.adapters.in.web.profile;

import com.mat.hexa_finance.application.ports.in.profile.UpdateEmailUseCase;
import com.mat.hexa_finance.application.ports.in.profile.UpdatePasswordUseCase;
import com.mat.hexa_finance.application.ports.in.profile.UpdateUsernameUseCase;
import com.mat.hexa_finance.domain.model.User;
import com.mat.hexa_finance.infrastructure.adapters.in.web.auth.model.CustomUserDetails;
import com.mat.hexa_finance.infrastructure.adapters.in.web.profile.dto.ProfileResponse;
import com.mat.hexa_finance.infrastructure.adapters.in.web.profile.mapper.ProfileWebMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UpdateUsernameUseCase updateUsernameUseCase;
    private final UpdatePasswordUseCase updatePasswordUseCase;
    private final UpdateEmailUseCase updateEmailUseCase;

    public ProfileController(UpdateUsernameUseCase updateUsernameUseCase, UpdatePasswordUseCase updatePasswordUseCase, UpdateEmailUseCase updateEmailUseCase) {
        this.updateUsernameUseCase = updateUsernameUseCase;
        this.updatePasswordUseCase = updatePasswordUseCase;
        this.updateEmailUseCase = updateEmailUseCase;
    }


    @GetMapping
    public String getProfile(@AuthenticationPrincipal CustomUserDetails userDetails, Model model){

        ProfileResponse response = ProfileWebMapper.toResponse(userDetails);
        model.addAttribute("currentUser", response);
        model.addAttribute("content", "profile/view");

        return "layouts/main-layout";
    }

    @GetMapping("/change-email")
    public String getUpdateEmailModal(@AuthenticationPrincipal CustomUserDetails userDetails, Model model){

        ProfileResponse response = ProfileWebMapper.toResponse(userDetails);
        model.addAttribute("currentUser", response);

        return "profile/fragments/profile-fragments :: change-email-modal";
    }

    @GetMapping("/change-username")
    public String getUpdateUsernameModal(@AuthenticationPrincipal CustomUserDetails userDetails, Model model){

        ProfileResponse response = ProfileWebMapper.toResponse(userDetails);
        model.addAttribute("currentUser", response);

        return "profile/fragments/profile-fragments :: change-username-modal";
    }

    @GetMapping("/change-password")
    public String getUpdatePasswordModal(@AuthenticationPrincipal CustomUserDetails userDetails, Model model){

        ProfileResponse response = ProfileWebMapper.toResponse(userDetails);
        model.addAttribute("currentUser", response);

        return "profile/fragments/profile-fragments :: change-password-modal";
    }

    @PostMapping("/update-username")
    public String updateUsername(@AuthenticationPrincipal CustomUserDetails userDetails,
                                 @RequestParam String newUsername,
                                 @RequestParam String password,
                                 Model model,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws ServletException {

        User updatedUser = updateUsernameUseCase.updateUserName(userDetails.getId(), password, newUsername.trim());

        response.setHeader("HX-Trigger", "{\"preSaveToast\": \"Tus datos han cambiado. Por seguridad, vuelve a ingresar.\"}");

        request.getSession().invalidate();
        SecurityContextHolder.clearContext();

        response.setHeader("HX-Redirect", "/login");

        return null;
    }

    @PostMapping("/update-email")
    public String updateEmail(@AuthenticationPrincipal CustomUserDetails userDetails,
                                 @RequestParam String newEmail,
                                 @RequestParam String password,
                                 Model model,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws ServletException {

        User updatedUser = updateEmailUseCase.updateEmail(userDetails.getId(), password, newEmail.trim());

        response.setHeader("HX-Trigger", "{\"preSaveToast\": \"Tus datos han cambiado. Por seguridad, vuelve a ingresar.\"}");

        request.getSession().invalidate();
        SecurityContextHolder.clearContext();

        response.setHeader("HX-Redirect", "/login");

        return null;
    }

    @PostMapping("/update-password")
    public String updatePassword(@AuthenticationPrincipal CustomUserDetails userDetails,
                                 @RequestParam String newPassword,
                                 @RequestParam String currentPassword,
                                 Model model,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws ServletException {

        User updatedUser = updatePasswordUseCase.updatePassword(userDetails.getId(), currentPassword, newPassword.trim());

        response.setHeader("HX-Trigger", "{\"preSaveToast\": \"Tus datos han cambiado. Por seguridad, vuelve a ingresar.\"}");

        request.getSession().invalidate();
        SecurityContextHolder.clearContext();

        response.setHeader("HX-Redirect", "/login");

        return null;
    }
}
