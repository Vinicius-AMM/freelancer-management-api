package com.manager.freelancer_management_api.exceptions;

public class UnauthorizedAccessException extends BusinessException {
    public UnauthorizedAccessException() {
        super("Access denied.");
    }
}
