package com.mat.hexa_finance.infrastructure.adapters.in.web.dashboard;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/")
    public String showIndex(Authentication auth, Model model) {
        // Spring Security pone un objeto AnonymousAuthenticationToken si no hay login
        // por eso es mejor chequear si el principal es una instancia de tu UserDetails

        boolean isAuthenticated = auth != null && auth.isAuthenticated()
                && !(auth instanceof AnonymousAuthenticationToken);

        if (isAuthenticated) {
            model.addAttribute("username", auth.getName()); // Para el saludo
            model.addAttribute("content", "dashboard/dashboard :: dashboard-content");
        } else {
            model.addAttribute("content", "pages/welcome :: welcome-content");
        }

        return "layouts/main-layout";
    }
}
