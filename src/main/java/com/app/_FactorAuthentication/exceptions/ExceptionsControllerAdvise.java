package com.app._FactorAuthentication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ExceptionsControllerAdvise {

    @ExceptionHandler
    public ResponseEntity<CustomErrorResponse> getErrorResponse(InvalidOtpException e){
        CustomErrorResponse errorResponse = new CustomErrorResponse();
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setMessage(e.getMessage());
        errorResponse.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler
    public ResponseEntity<CustomErrorResponse> getErrorResponse(UserAlreadyExistsException e){
        CustomErrorResponse errorResponse = new CustomErrorResponse();
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setMessage(e.getMessage());
        errorResponse.setTimestamp(LocalDateTime.now());
        System.out.println(errorResponse);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<CustomErrorResponse> getErrorResponse(UserNotFoundException e){
        CustomErrorResponse errorResponse = new CustomErrorResponse();
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setMessage(e.getMessage());
        errorResponse.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler
    public ResponseEntity<CustomErrorResponse> getErrorResponse(Exception e) {
        System.out.println("Something went wrong..."+ e);
        CustomErrorResponse errorResponse = new CustomErrorResponse();
        errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.setMessage(e.getMessage());
        errorResponse.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
