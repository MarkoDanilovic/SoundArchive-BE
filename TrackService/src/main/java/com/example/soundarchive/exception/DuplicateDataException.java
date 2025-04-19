package com.example.soundarchive.exception;

import org.springframework.stereotype.Component;

@Component
public class DuplicateDataException extends RuntimeException{

    public DuplicateDataException(){}

    public DuplicateDataException(String message){
        super(message);
    }
}
