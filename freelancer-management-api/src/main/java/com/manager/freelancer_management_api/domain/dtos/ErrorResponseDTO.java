package com.manager.freelancer_management_api.domain.dtos;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponseDTO {
    private int statusCode;
    private String message;
    private LocalDateTime timestamp;
}
