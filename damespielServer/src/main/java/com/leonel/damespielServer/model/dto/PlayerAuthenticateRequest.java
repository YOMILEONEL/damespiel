package com.leonel.damespielServer.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public record PlayerAuthenticateRequest(

        Long id,

        @NotNull(message = "Name is required")
        String name,

        @NotNull(message = "Password is required")
        @Size(min = 6, max = 16, message = "Password must be between 6 and 16 characters long")
        String password
) {
}
