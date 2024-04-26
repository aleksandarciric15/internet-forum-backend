package com.example.internetforum.advices;

import com.example.internetforum.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(AlreadyExistsUsernameException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public void handleAlreadyExistsUsernameException(){}

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleBadRequestException(){
    }

    @ExceptionHandler(BlockedAccountException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public void handleBlockedAccountException(){}

    @ExceptionHandler(GithubApiException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleGithubException(){}

    @ExceptionHandler(NotApprovedAccountException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public void handleNotApprovedAccountException(){}

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleNotFoundException(){}

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public void handleUnauthorizedException(){}
}
