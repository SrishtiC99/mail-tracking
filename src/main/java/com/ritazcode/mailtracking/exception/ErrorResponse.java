package com.ritazcode.mailtracking.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Data
@Schema(description = "Custom error message that is shown when an exception is thrown")
public class ErrorResponse {
    @Schema(description = "exception name")
    private String name;

    @Schema(description = "exception message")
    private String message;

    @Schema(description = "exception trace")
    private String trace;

    @Schema(description = "exception uri")
    private String uri;

    @Schema(description = "time of throwing the exception ")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyy hh:mm:ss")
    private Date timestamp;

    @Schema(description = "validation errors of object fields")
    private List<ValidationError> validationErrors;

    public ErrorResponse() {
        timestamp = new Date();
        validationErrors = new ArrayList<>();
    }

    public ErrorResponse(String name, String message, String trace, String uri) {
        this();
        setName(name);
        setMessage(message);
        setTrace(trace);
        setUri(uri);
    }

    public void addValidationError(String field, String message) {
        if (Objects.isNull(validationErrors))
            validationErrors = new ArrayList<>();
        validationErrors.add(new ValidationError(field, message));
    }


    @Schema(description = "validation errors of fields")
    @Getter
    @Setter
    @RequiredArgsConstructor
    @Builder
    private static class ValidationError {

        @Schema(description = "field")
        private final String field;

        @Schema(description = "message about field validation")
        private final String message;

    }
}