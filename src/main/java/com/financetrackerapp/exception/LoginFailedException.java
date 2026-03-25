package com.financetrackerapp.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class LoginFailedException extends RuntimeException {
    String message;
    public LoginFailedException(){message="Try Again";}
    public LoginFailedException(String message) {
        super();
        this.message=message;
    }
    @Override
    public String toString(){return "Invalid Credentials" + message;}
}
