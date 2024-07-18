package com.example.studentdetails.exception;

import lombok.Data;

@Data
public class APIException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String message;


    public APIException() {
    }

    public APIException(String message) {
        this.message=message;
    }
}
