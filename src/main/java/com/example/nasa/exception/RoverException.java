package com.example.nasa.exception;

public class RoverException extends RuntimeException {
    public RoverException(String exception) {
        super("Photos not found for date " + exception);
    }
}
