package com.example.demo.Exceptions;

public class UsernameAlreadyExists extends Exception{
    public UsernameAlreadyExists(String message) {
        super(message);
    }
}
