package com.mat.hexa_finance.infrastructure.adapters.in.web.dashboard;

import com.mat.hexa_finance.application.ports.in.dashboard.GetDashboardSummaryQuery;
import com.mat.hexa_finance.domain.model.DashboardSummary;
import com.mat.hexa_finance.infrastructure.adapters.in.web.auth.model.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class DashboardController {

    private final GetDashboardSummaryQuery dashboardSummaryQuery;

    public DashboardController(GetDashboardSummaryQuery dashboardSummaryQuery) {
        this.dashboardSummaryQuery = dashboardSummaryQuery;
    }

    @GetMapping("/")
    public String showIndex(@AuthenticationPrincipal CustomUserDetails user,
                            @RequestParam(required = false) String month,
                            Model model) {

        boolean isAuthenticated = user != null;

        if (isAuthenticated) {
            int yearValue;
            int monthValue;
            String selectedMonth;

            if (month == null || month.isEmpty()) {
                LocalDate now = LocalDate.now();
                yearValue = now.getYear();
                monthValue = now.getMonthValue();
                selectedMonth = String.format("%d-%02d", yearValue, monthValue);
            } else {
                String[] parts = month.split("-");
                yearValue = Integer.parseInt(parts[0]);  // "2026" -> 2026
                monthValue = Integer.parseInt(parts[1]); // "04" -> 4
                selectedMonth = month;
            }

            DashboardSummary summary = dashboardSummaryQuery.execute(user.getId(), monthValue, yearValue);

            model.addAttribute("username", user.getUsername());
            model.addAttribute("summary", summary);
            model.addAttribute("selectedMonth", selectedMonth);
            model.addAttribute("content", "dashboard/dashboard :: dashboard-content");
        } else {
            model.addAttribute("content", "pages/welcome :: welcome-content");
        }

        return "layouts/main-layout";
    }
}
