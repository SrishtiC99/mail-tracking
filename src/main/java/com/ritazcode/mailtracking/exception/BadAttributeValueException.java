package com.ritazcode.mailtracking.exception;

import org.springframework.http.HttpStatus;

/**
 * custom bad attribute value exception, thrown when the passed attribute is not valid
 */
public class BadAttributeValueException extends ApiBaseException {
    public BadAttributeValueException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.BAD_REQUEST;
    }
}
