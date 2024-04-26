package com.example.internetforum.exceptions;

public class GithubApiException extends RuntimeException{
    public GithubApiException(){}
    public GithubApiException(String message){
        super(message);
    }
}
