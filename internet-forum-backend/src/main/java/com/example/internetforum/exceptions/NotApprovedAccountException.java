package com.example.internetforum.exceptions;

public class NotApprovedAccountException extends RuntimeException {
    public NotApprovedAccountException(){}
    public NotApprovedAccountException(String message){
        super(message);
    }
}
