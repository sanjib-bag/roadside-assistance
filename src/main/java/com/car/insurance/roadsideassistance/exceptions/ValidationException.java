package com.car.insurance.roadsideassistance.exceptions;

/**
 * Exception class to throw Validation related exceptions
 */
public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }
}
