package com.example.internetforum.exceptions;

public class BlockedAccountException extends RuntimeException {
    public BlockedAccountException(){}
    public BlockedAccountException(String message){
        super(message);
    }
}
