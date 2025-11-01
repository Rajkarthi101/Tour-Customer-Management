package com.tms.customer.exceptions;

public class DuplicateBookingException extends Exception {
    
    public DuplicateBookingException(String message) {
        super(message);
    }
}
