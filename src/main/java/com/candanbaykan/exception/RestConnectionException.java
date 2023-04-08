package com.candanbaykan.exception;

public class RestConnectionException extends RuntimeException {
    public RestConnectionException() {
        super("Couldn't connect to backend server!");
    }
}
