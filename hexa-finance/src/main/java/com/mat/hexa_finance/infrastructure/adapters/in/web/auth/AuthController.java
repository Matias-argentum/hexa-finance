package com.mat.hexa_finance.infrastructure.adapters.in.web.auth;

import com.mat.hexa_finance.application.ports.in.user.RegisterUserUseCase;
import com.mat.hexa_finance.application.ports.in.user.command.RegisterUserCommand;
import com.mat.hexa_finance.infrastructure.adapters.in.web.common.utils.HtmxUtils.HtmxUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
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

    public AuthController(RegisterUserUseCase registerUserUseCase) {
        this.registerUserUseCase = registerUserUseCase;
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
