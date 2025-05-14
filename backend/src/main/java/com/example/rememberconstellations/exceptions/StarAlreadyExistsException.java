package com.example.rememberconstellations.exceptions;

public class StarAlreadyExistsException extends RuntimeException {
    public StarAlreadyExistsException(String message) {
        super(message);
    }
}
