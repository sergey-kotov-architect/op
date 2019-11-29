package com.sergeykotov.op.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "failed to extract from the database")
public class ExtractionException extends RuntimeException {
    public ExtractionException(String message, Throwable cause) {
        super(message, cause);
    }
}