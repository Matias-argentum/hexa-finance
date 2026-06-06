package com.mat.hexa_finance.infrastructure.adapters.in.web.config;

import com.mat.hexa_finance.infrastructure.adapters.in.web.interceptors.ForcePasswordChangeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final ForcePasswordChangeInterceptor forcePasswordChangeInterceptor;

    // inyecta el interceptor
    public WebMvcConfig(ForcePasswordChangeInterceptor forcePasswordChangeInterceptor) {
        this.forcePasswordChangeInterceptor = forcePasswordChangeInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(forcePasswordChangeInterceptor)
                .addPathPatterns("/**") // Que escuche todas las rutas de la app
                .excludePathPatterns("/css/**", "/js/**", "/fonts/**", "/images/**"); //  excluye los estáticos para que no se rompa el diseño
    }

}
