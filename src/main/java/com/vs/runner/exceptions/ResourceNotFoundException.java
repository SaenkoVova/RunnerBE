package com.vs.runner.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super("Resource not found: " + message);
    }
}
