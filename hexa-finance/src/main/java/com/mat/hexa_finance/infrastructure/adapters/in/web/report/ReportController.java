package com.mat.hexa_finance.infrastructure.adapters.in.web.report;

import com.mat.hexa_finance.application.ports.in.category.FindAllCategoriesUseCase;
import com.mat.hexa_finance.application.ports.in.report.GetReportUseCase;
import com.mat.hexa_finance.application.ports.in.report.query.ReportFilter;
import com.mat.hexa_finance.domain.model.FinancialReport;
import com.mat.hexa_finance.domain.model.TransactionType;
import com.mat.hexa_finance.infrastructure.adapters.in.web.auth.model.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/reports")
public class ReportController {

    private final GetReportUseCase getReportUseCase;
    private final FindAllCategoriesUseCase findAllCategoriesUseCase; // Para llenar los checkboxes

    public ReportController(GetReportUseCase getReportUseCase, FindAllCategoriesUseCase findAllCategoriesUseCase) {
        this.getReportUseCase = getReportUseCase;
        this.findAllCategoriesUseCase = findAllCategoriesUseCase;
    }

    @GetMapping
    public String reportPage(Model model, @AuthenticationPrincipal CustomUserDetails user) {
        model.addAttribute("categories", findAllCategoriesUseCase.executeSearch(user.getId()));
        model.addAttribute("types", TransactionType.values());
        model.addAttribute("content", "report/reports :: report-content");
        return "layouts/main-layout";
    }

    @GetMapping("/search")
    public String searchReport(
            ReportFilter filter,
            @AuthenticationPrincipal CustomUserDetails user,
            Model model) {

        FinancialReport report = getReportUseCase.execute(user.getId(), filter);
        System.out.println(report);
        model.addAttribute("report", report);

        // Retornamos solo el fragmento de la tabla para HTMX
        return "report/fragments/report-table :: results";
    }
}