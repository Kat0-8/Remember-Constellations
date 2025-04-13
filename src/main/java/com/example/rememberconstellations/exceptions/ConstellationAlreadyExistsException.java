package com.example.rememberconstellations.exceptions;

public class ConstellationAlreadyExistsException extends RuntimeException {
    public ConstellationAlreadyExistsException(String message) {
        super(message);
    }
}
