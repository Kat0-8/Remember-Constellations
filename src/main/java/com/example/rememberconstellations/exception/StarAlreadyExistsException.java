package com.example.rememberconstellations.exception;

public class StarAlreadyExistsException extends RuntimeException {
    public StarAlreadyExistsException(String message) {
        super(message);
    }
}
