package com.mat.hexa_finance.infrastructure.adapters.in.web.auth;

import com.mat.hexa_finance.application.ports.in.profile.UpdatePasswordUseCase;
import com.mat.hexa_finance.application.ports.in.user.RegisterUserUseCase;
import com.mat.hexa_finance.application.ports.in.user.command.RegisterUserCommand;
import com.mat.hexa_finance.infrastructure.adapters.in.web.auth.model.CustomUserDetails;
import com.mat.hexa_finance.infrastructure.adapters.in.web.common.utils.HtmxUtils.HtmxUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final UpdatePasswordUseCase updatePasswordUseCase;

    public AuthController(RegisterUserUseCase registerUserUseCase, UpdatePasswordUseCase updatePasswordUseCase) {
        this.registerUserUseCase = registerUserUseCase;
        this.updatePasswordUseCase = updatePasswordUseCase;
    }

    @GetMapping("/login")
    public String login(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            return "redirect:/";
        }

        model.addAttribute("content", "auth/login");
        return "layouts/main-layout";
    }

    @GetMapping("/register")
    public String register(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            return "redirect:/";
        }

        model.addAttribute("content", "auth/register");
        return "layouts/main-layout";
    }

    @GetMapping("/force-change-password")
    public String forcePasswordChange(){
        return "auth/force-password-change";
    }

    @PostMapping("/auth/force-change-password")
    public String processForceChangePassword(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            Model model,
            HttpServletRequest request,
            HttpServletResponse response) {

        //System.out.println("ENTRE A ESTE CONTROLLER???????????????");

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "La nueva contraseña y su confirmación no coinciden.");
            return "auth/force-change-password :: form";
        }

        try {
            updatePasswordUseCase.updatePassword(userDetails.getId(), currentPassword, newPassword.trim());

            response.setHeader("HX-Redirect", "/login?changed=true");

            request.getSession().invalidate();
            SecurityContextHolder.clearContext();

            return null;

        } catch (Exception e) {
            model.addAttribute("error", "La contraseña temporal ingresada es incorrecta.");
            return "auth/force-change-password :: form";
        }
    }

    @PostMapping("/register")
    @ResponseBody
    public void register(@RequestParam String username,
                           @RequestParam String email,
                           @RequestParam String password,
                           HttpServletResponse response) {
        RegisterUserCommand command = new RegisterUserCommand(username, email, password);

        registerUserUseCase.execute(command);

        HtmxUtils.addNotification(response, "Registro Exitoso!");

        response.addHeader("HX-Redirect", "/login?regSuccess=true");
    }
}
