package com.himedia.spserver.controller;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public void handleValidationExceptions(MethodArgumentNotValidException ex) {
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            System.out.println("[VALIDATION ERROR] " + error.getField() + " 필드: "
                    + error.getDefaultMessage());
        });
    }
}