package com.example.internetforum.exceptions;

public class UnauthorizedException extends RuntimeException{
    public UnauthorizedException(){}
    public UnauthorizedException(String message){
        super(message);
    }
}
