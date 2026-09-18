package com.spring.urlshortner.exception;

public class MaxCollisionException extends Exception {

    public MaxCollisionException(String message) {
        super(message);
    }

    public MaxCollisionException(String message, Throwable cause) {
        super(message, cause);
    }
}
