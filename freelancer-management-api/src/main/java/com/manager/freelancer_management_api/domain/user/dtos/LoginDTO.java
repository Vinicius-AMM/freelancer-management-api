package com.manager.freelancer_management_api.domain.user.dtos;

public record LoginDTO(
        String email,
        String password
) {
}
