package com.candanbaykan.exception;

public class NoRocketException extends RuntimeException {
    public NoRocketException() {
        super("There aren't any rockets to display!");
    }
}
