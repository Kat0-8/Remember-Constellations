package com.example.rememberconstellations.exception;

public class ConstellationAlreadyExistsException extends RuntimeException {
    public ConstellationAlreadyExistsException(String message) {
        super(message);
    }
}
