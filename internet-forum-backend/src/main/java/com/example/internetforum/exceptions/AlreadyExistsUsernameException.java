package com.example.internetforum.exceptions;

public class AlreadyExistsUsernameException extends RuntimeException {
    public AlreadyExistsUsernameException(){}
    public AlreadyExistsUsernameException(String message){
        super(message);
    }
}
