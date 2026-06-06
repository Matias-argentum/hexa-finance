package com.mat.hexa_finance.infrastructure.adapters.in.web.interceptors;

import com.mat.hexa_finance.infrastructure.adapters.in.web.auth.model.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ForcePasswordChangeInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        //si no esta logueado lo deja pasar
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymous")) {
            return true;
        }

        if (auth.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();

            boolean mustChangePassword = userDetails.isForcePasswordChange();
            if (mustChangePassword) {
                // si tiene que cambiar el password y va a los lugares correctos pasa
                if (requestURI.equals("/auth/force-change-password") || requestURI.equals("/force-change-password") || requestURI.equals("/logout")) {
                    return true;
                }
                // si intenta ir a cualquier otro lado, va  aal pantalla de cambio
                response.sendRedirect("/force-change-password");
                return false; // corta la petición acá, no llega al controller
            } else {
                //si no tiene que cambiar el password
                if (requestURI.equals("/force-change-password")) {
                    response.sendRedirect("/"); // va al home
                    return false;
                }
            }
        }


        return true; // la peiticon sigue su curso aca con este true si no ortó antes
    }
}
