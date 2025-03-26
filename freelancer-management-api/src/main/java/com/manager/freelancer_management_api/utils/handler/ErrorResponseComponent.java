package com.manager.freelancer_management_api.utils.handler;

import com.manager.freelancer_management_api.domain.dtos.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ErrorResponseComponent {
    public ErrorResponseDTO build(HttpStatus status, String message) {
        return ErrorResponseDTO.builder()
                .statusCode(status.value())
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public ResponseEntity<ErrorResponseDTO> buildResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(build(status, message));
    }
}
