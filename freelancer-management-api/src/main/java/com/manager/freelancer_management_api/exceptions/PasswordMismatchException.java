package com.manager.freelancer_management_api.exceptions;

public class PasswordMismatchException extends BusinessException {
    public PasswordMismatchException() {
        super("Wrong password.");
    }
    public PasswordMismatchException(String message) {
        super(message);
    }
}
