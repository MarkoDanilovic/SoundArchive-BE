package com.example.soundarchive.exception;

public class UnauthorizedAccessException extends RuntimeException{

    public UnauthorizedAccessException() {
    }

    public UnauthorizedAccessException(String message) {
        super(message);
    }
}
