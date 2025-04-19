package com.example.soundarchive.exception;

import org.springframework.stereotype.Component;

@Component
public class DataNotFoundException extends RuntimeException{

    public DataNotFoundException() {

    }

    public DataNotFoundException(String message) { super(message);}
}