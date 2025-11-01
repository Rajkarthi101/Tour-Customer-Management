package com.tms.customer.models.errors;

import java.util.List;

import lombok.Data;

@Data
public class ErrorObject {
    
    private String status;
    private String message;
    private String timestamp;
    private List<String> errors;
}
