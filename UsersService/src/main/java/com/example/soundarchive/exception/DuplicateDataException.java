package com.example.soundarchive.exception;

public class DuplicateDataException extends RuntimeException{

    public DuplicateDataException() {
    }

    public DuplicateDataException(String message) {
        super(message);
    }
}
