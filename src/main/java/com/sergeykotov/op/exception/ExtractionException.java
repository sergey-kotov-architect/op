package com.sergeykotov.op.exception;

public class ExtractionException extends RuntimeException {
    public ExtractionException(String message, Throwable cause) {
        super(message, cause);
    }
}