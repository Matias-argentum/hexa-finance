package com.mat.hexa_finance.application.ports.in.user.command;

public record RegisterUserCommand(
        String username,
        String email,
        String password
) {
}
