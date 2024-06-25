package com.vs.runner.exceptions;

public class InternalServerError extends RuntimeException {
    public InternalServerError(String reason) {
        super("Internal server error: " + reason);
    }
}
