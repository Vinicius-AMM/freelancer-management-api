package com.manager.freelancer_management_api.domain.user.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record LoginDTO(
        @NotNull(message = "Fill in this field")
        @Email(message = "Invalid email format")
        String email,
        @NotNull(message = "Fill in this field")
        String password
) {
}
