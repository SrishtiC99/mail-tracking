package com.ritazcode.mailtracking.exception;

import org.springframework.http.HttpStatus;

/**
 * Base exception class. This abstract class is used in exception handling of Run time exceptions
 */
public abstract class ApiBaseException extends RuntimeException {

    public ApiBaseException(String message) {
        super(message);
    }

    public abstract HttpStatus getStatusCode();
}
