package com.manager.freelancer_management_api.infra.handler;

import com.manager.freelancer_management_api.domain.dtos.ErrorResponseDTO;
import com.manager.freelancer_management_api.exceptions.*;
import com.manager.freelancer_management_api.utils.handler.ErrorResponseComponent;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;



@ControllerAdvice
@Import(ErrorResponseComponent.class)
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final ErrorResponseComponent errorResponseComponent;

    public GlobalExceptionHandler(ErrorResponseComponent errorResponseComponent) {
        this.errorResponseComponent = errorResponseComponent;
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseDTO> businessExceptionHandler(BusinessException e){
        return errorResponseComponent.buildResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }
}
