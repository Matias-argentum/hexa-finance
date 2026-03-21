package com.mat.hexa_finance.infrastructure.adapters.in.web.auth;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("content", "auth/login");
        return "layouts/main-layout";
    }
}
