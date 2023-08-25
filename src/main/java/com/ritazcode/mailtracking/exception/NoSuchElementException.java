package com.ritazcode.mailtracking.exception;

import org.springframework.http.HttpStatus;

/**
 * custom no such element exception, thrown when an element is not found
 */
public class NoSuchElementException extends ApiBaseException {

    public NoSuchElementException(String message) {
        super(message);
    }

    public HttpStatus getStatusCode() {
        return HttpStatus.NOT_FOUND;
    }
}