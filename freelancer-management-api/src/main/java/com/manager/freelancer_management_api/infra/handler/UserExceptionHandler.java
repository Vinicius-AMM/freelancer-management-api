package com.manager.freelancer_management_api.infra.handler;

import com.manager.freelancer_management_api.domain.dtos.ErrorResponseDTO;
import com.manager.freelancer_management_api.domain.user.exceptions.*;
import com.manager.freelancer_management_api.exceptions.PasswordMismatchException;
import com.manager.freelancer_management_api.utils.handler.ErrorResponseComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Import(ErrorResponseComponent.class)
public class UserExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String INVALID_CREDENTIALS_MESSAGE = "Invalid email or password.";

    private final ErrorResponseComponent errorResponseComponent;

    @Autowired
    public UserExceptionHandler(ErrorResponseComponent errorResponseComponent) {
        this.errorResponseComponent = errorResponseComponent;
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> userNotFoundHandler(UserNotFoundException e){
        return errorResponseComponent.buildResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> emailAlreadyExistsHandler(EmailAlreadyExistsException e){
        return errorResponseComponent.buildResponse(HttpStatus.CONFLICT, "Invalid email address.");
    }

    @ExceptionHandler(DocumentAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> documentAlreadyExistsHandler(DocumentAlreadyExistsException e){
        return errorResponseComponent.buildResponse(HttpStatus.CONFLICT, "Invalid document.");
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<ErrorResponseDTO> passwordMismatchHandler(PasswordMismatchException e){
        return errorResponseComponent.buildResponse(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(InvalidUserRoleException.class)
    public ResponseEntity<ErrorResponseDTO> invalidUserRoleHandler(InvalidUserRoleException e){
        return errorResponseComponent.buildResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(SamePasswordException.class)
    public ResponseEntity<ErrorResponseDTO> samePasswordHandler(SamePasswordException e){
        return errorResponseComponent.buildResponse(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDTO> badCredentialsHandler(BadCredentialsException e){
        return errorResponseComponent.buildResponse(HttpStatus.BAD_REQUEST, INVALID_CREDENTIALS_MESSAGE);
    }

    @ExceptionHandler({UsernameNotFoundException.class})
    public ResponseEntity<ErrorResponseDTO> usernameNotFoundExceptionHandler(UsernameNotFoundException e){
        return errorResponseComponent.buildResponse(HttpStatus.UNAUTHORIZED, INVALID_CREDENTIALS_MESSAGE);
    }

    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<ErrorResponseDTO> invalidEmailHandler(InvalidEmailException e){
        return errorResponseComponent.buildResponse(HttpStatus.UNAUTHORIZED, INVALID_CREDENTIALS_MESSAGE);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ErrorResponseDTO> invalidPasswordHandler(InvalidPasswordException e){
        return errorResponseComponent.buildResponse(HttpStatus.UNAUTHORIZED, INVALID_CREDENTIALS_MESSAGE);
    }
}
