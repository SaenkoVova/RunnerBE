package com.vs.runner.exceptions;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String reason) {
        super("Bad request: " + reason);
    }
}
