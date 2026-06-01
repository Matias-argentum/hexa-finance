package com.mat.hexa_finance.infrastructure.adapters.in.web.advice;

import com.mat.hexa_finance.domain.exception.*;
import com.mat.hexa_finance.infrastructure.adapters.in.web.common.utils.HtmxUtils.HtmxUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(UserNotFoundException ex, HttpServletResponse response) {
        response.setHeader("HX-Trigger", "{\"preSaveToast\": \"El usuario no existe.\"}");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<String> handleIncorrectPassword(IncorrectPasswordException ex, HttpServletResponse response) {
        response.setHeader("HX-Trigger", "{\"preSaveToast\": \"La contraseña actual es incorrecta.\"}");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<String> handleCategoryNotFoundException(CategoryNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Void> handleEmailAlreadyExists(EmailAlreadyExistsException ex, HttpServletResponse response) {
        response.setHeader("HX-Trigger", "{\"preSaveToast\": \"Error: Email ya existe.\"}");
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(BadUsernameException.class)
    public ResponseEntity<Void> badUsername(BadUsernameException ex, HttpServletResponse response) {
        response.setHeader("HX-Trigger", "{\"preSaveToast\": \"El nombre de usuario debe ser una sola palabra.\"}");
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler({AuthorizationDeniedException.class, org.springframework.security.access.AccessDeniedException.class})
    public String handleAccessDenied() {
        return "redirect:/?error=unauthorized";
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public String handle404(NoResourceFoundException ex, HttpServletResponse response) {
        response.setStatus(HttpStatus.NOT_FOUND.value());
        return "error/404"; // Spring busca directo en templates/error/404.html
    }

    // Captura cualquier otra explosión (500)
    @ExceptionHandler(Exception.class)
    public String handle500(Exception ex, HttpServletResponse response) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return "error/500";
    }

}
