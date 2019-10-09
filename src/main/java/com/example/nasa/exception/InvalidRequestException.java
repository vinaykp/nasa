package com.example.nasa.exception;

public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String message) {
        super("Invalid Request date/format(yyyy-MM-dd) " + message);
    }
}
