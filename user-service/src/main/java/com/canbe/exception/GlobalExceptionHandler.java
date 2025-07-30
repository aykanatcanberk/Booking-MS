package com.canbe.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception exception , WebRequest webRequest){

        ExceptionResponse exceptionResponse = new ExceptionResponse(
                exception.getMessage(),
                webRequest.getDescription(false),
                LocalDateTime.now()
        );

        return ResponseEntity.ok(exceptionResponse);
    }
}
