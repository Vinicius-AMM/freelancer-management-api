package com.manager.freelancer_management_api.domain.user.dtos;

public record PasswordUpdateRequest(String oldPassword, String newPassword) {
}
