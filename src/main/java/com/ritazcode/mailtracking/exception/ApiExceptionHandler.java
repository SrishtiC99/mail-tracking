package com.ritazcode.mailtracking.exception;

import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Custom Exception handling class
 */
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * handle custom ApiExceptions
     * @param ex thrown exception
     * @param request sent request
     * @return Error response that contains information about the thrown exception
     */
    @ExceptionHandler(value = ApiBaseException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiBaseException ex, WebRequest request) {
        return new ResponseEntity<>(
                new ErrorResponse(
                        ex.getClass().getSimpleName(),
                        ex.getMessage(),
                        ex.getStackTrace().toString(),
                        request.getDescription(true))
                , ex.getStatusCode());
    }

    /**
     * handle all uncaught custom exceptions
     * @param ex thrown exception
     * @param request sent request
     * @return Error response that contains information about the thrown exception
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleApiException(Exception ex, WebRequest request) {
        return new ResponseEntity<>(
                new ErrorResponse(
                        ex.getClass().getSimpleName(),
                        ex.getMessage(),
                        ex.getStackTrace().toString(),
                        request.getDescription(true))
                , HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * handles method argument not valid exception
     * @param ex the exception to handle
     * @param headers the headers to be written to the response
     * @param status the selected response status
     * @param request the current request
     * @return Error response that contains information about the thrown exception, with list of detailed info about not valid attribute
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, @NotNull HttpHeaders headers, @NotNull   HttpStatusCode status, WebRequest request) {
        ErrorResponse response = new ErrorResponse(
                ex.getClass().getSimpleName(),
                ex.getMessage(),
                ex.getStackTrace().toString(),
                request.getDescription(false));
        ex.getBindingResult().getFieldErrors().forEach(error -> response.addValidationError(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(response, status);
    }


}

