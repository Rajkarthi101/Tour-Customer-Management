package com.tms.customer.advice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.tms.customer.exceptions.CustomerNotFoundException;
import com.tms.customer.exceptions.DuplicateBookingException;
import com.tms.customer.exceptions.InvalidBookingDate;
import com.tms.customer.models.errors.ErrorObject;

@RestControllerAdvice
public class GlobalExceptionHandler {
        

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorObject> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        // list all errors
        List<String> errors = new ArrayList<String>();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            errors.add(fieldError.getDefaultMessage());
        }
        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatus("400");
        errorObject.setMessage("Validation failed");
        errorObject.setTimestamp(new Date().toString());
        errorObject.setErrors(errors);
        return ResponseEntity.ok().body(errorObject);
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ErrorObject> handleCustomerNotFoundException(CustomerNotFoundException e) {
        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatus("404");
        errorObject.setMessage(e.getMessage());
        errorObject.setTimestamp(new Date().toString());
        return ResponseEntity.badRequest().body(errorObject);
    }

    @ExceptionHandler(DuplicateBookingException.class)
    public ResponseEntity<ErrorObject> handleDuplicateBookingException(DuplicateBookingException e) {
        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatus("400");
        errorObject.setMessage(e.getMessage());
        errorObject.setTimestamp(new Date().toString());
        return ResponseEntity.badRequest().body(errorObject);
    }

    @ExceptionHandler(InvalidBookingDate.class)
    public ResponseEntity<ErrorObject> handleInvalidBookingDate(InvalidBookingDate e) {
        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatus("400");
        errorObject.setMessage(e.getMessage());
        errorObject.setTimestamp(new Date().toString());
        return ResponseEntity.badRequest().body(errorObject);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorObject> handleException(Exception e) {
        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatus("500");
        errorObject.setMessage(e.getMessage());
        errorObject.setTimestamp(new Date().toString());
        return ResponseEntity.internalServerError().body(errorObject);
    }
}
