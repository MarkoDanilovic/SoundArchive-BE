package com.example.soundarchive.exception;

import org.springframework.stereotype.Component;

@Component
public class InternalServerErrorException extends RuntimeException{

    public InternalServerErrorException() {

    }

    public InternalServerErrorException(String message) { super(message);}
}
