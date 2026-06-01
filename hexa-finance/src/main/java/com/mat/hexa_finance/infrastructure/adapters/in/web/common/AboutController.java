package com.mat.hexa_finance.infrastructure.adapters.in.web.common;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutController {

    @GetMapping("/about")
    public String about(Model model) {

        model.addAttribute("content", "pages/about");

        return "layouts/main-layout";
    }
}
