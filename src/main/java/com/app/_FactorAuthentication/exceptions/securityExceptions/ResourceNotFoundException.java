package com.app._FactorAuthentication.exceptions.securityExceptions;

public class ResourceNotFoundException extends RuntimeException{
    private String message;

    public ResourceNotFoundException() {
        super("Resource not found.");
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
