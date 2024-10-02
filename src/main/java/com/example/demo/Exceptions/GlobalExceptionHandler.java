package com.example.demo.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UsernameAlreadyExists.class)
    public ResponseEntity<String>UsernameAlreadyExistsException(UsernameAlreadyExists e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error con usuario:"+e.getMessage());
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String>HandleGenericException(Exception e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado:"+e.getMessage());
    }

}
