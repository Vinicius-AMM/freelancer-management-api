package com.manager.freelancer_management_api.infra.handler;

import com.manager.freelancer_management_api.domain.dtos.ErrorResponseDTO;
import com.manager.freelancer_management_api.domain.user.exceptions.*;
import com.manager.freelancer_management_api.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String INVALID_CREDENTIALS_MESSAGE = "Invalid email or password.";

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseDTO> businessExceptionHandler(BusinessException e){
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> userNotFoundHandler(UserNotFoundException e){
        return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> emailAlreadyExistsHandler(EmailAlreadyExistsException e){
        return buildErrorResponse(HttpStatus.CONFLICT, "Invalid email address.");
    }

    @ExceptionHandler(DocumentAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> documentAlreadyExistsHandler(DocumentAlreadyExistsException e){
        return buildErrorResponse(HttpStatus.CONFLICT, "Invalid document.");
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<ErrorResponseDTO> passwordMismatchHandler(PasswordMismatchException e){
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(InvalidUserRoleException.class)
    public ResponseEntity<ErrorResponseDTO> invalidUserRoleHandler(InvalidUserRoleException e){
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(SamePasswordException.class)
    public ResponseEntity<ErrorResponseDTO> samePasswordHandler(SamePasswordException e){
        return buildErrorResponse(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDTO> badCredentialsHandler(BadCredentialsException e){
        return buildErrorResponse(HttpStatus.BAD_REQUEST, INVALID_CREDENTIALS_MESSAGE);
    }

    @ExceptionHandler({UsernameNotFoundException.class})
    public ResponseEntity<ErrorResponseDTO> usernameNotFoundExceptionHandler(UsernameNotFoundException e){
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, INVALID_CREDENTIALS_MESSAGE);
    }

    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<ErrorResponseDTO> invalidEmailHandler(InvalidEmailException e){
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, INVALID_CREDENTIALS_MESSAGE);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ErrorResponseDTO> invalidPasswordHandler(InvalidPasswordException e){
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, INVALID_CREDENTIALS_MESSAGE);
    }

    private ResponseEntity<ErrorResponseDTO> buildErrorResponse(HttpStatus status, String message) {
        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .statusCode(status.value())
                .message(message)
                .timestamp(LocalDateTime.now())
               .build();
        return ResponseEntity.status(status).body(errorResponse);
    }
}
