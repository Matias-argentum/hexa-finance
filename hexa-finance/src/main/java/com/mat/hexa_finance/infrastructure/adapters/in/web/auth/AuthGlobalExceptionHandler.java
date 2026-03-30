package com.mat.hexa_finance.infrastructure.adapters.in.web.auth;

import com.mat.hexa_finance.domain.exception.EmailAlreadyExistsException;
import com.mat.hexa_finance.infrastructure.adapters.in.web.common.utils.HtmxUtils.HtmxUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AuthGlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Void> handleEmailAlreadyExists(EmailAlreadyExistsException ex, HttpServletResponse response) {
        HtmxUtils.addNotificationAndClose(response, ex.getMessage());
        return ResponseEntity.ok().build();
    }
}
