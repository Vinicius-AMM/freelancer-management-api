package com.manager.freelancer_management_api.domain.user.dtos;

import com.manager.freelancer_management_api.domain.user.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterUserDTO(
        @NotNull
        @Size(max = 100, message = "Full name must be at most 100 characters.")
        String fullName,

        @NotNull
        @Size(min = 11, max = 14)
        String document,

        @NotNull
        @Size(max = 100, message = "Email must be at most 100 characters.")
        @Email(message = "Enter a valid email.")
        String email,

        @NotNull
        @Size(min = 6, max = 100, message = "The password must have at least 6 characters.")
        String password,

        @NotNull(message = "Main user role cannot be null.")
        UserRole mainUserRole,

        UserRole currentUserRole
) {
}
