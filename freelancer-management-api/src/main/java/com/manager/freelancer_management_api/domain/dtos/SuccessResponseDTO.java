package com.manager.freelancer_management_api.domain.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SuccessResponseDTO {
    private final String message;
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final boolean success = true;

    public SuccessResponseDTO(String message) {
        this.message = message;
    }
}
