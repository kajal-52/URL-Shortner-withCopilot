package com.spring.urlshortner.exception;

/**
 * Signals that generating an unused short code exceeded the allowed collision attempts.
 */
public class MaxCollisionException extends Exception {

    /**
     * Creates the exception with a detail message.
     *
     * @param message explanation of the collision failure
     */
    public MaxCollisionException(String message) {
        super(message);
    }

    /**
     * Creates the exception with a detail message and underlying cause.
     *
     * @param message explanation of the collision failure
     * @param cause underlying reason for the failure
     */
    public MaxCollisionException(String message, Throwable cause) {
        super(message, cause);
    }
}
