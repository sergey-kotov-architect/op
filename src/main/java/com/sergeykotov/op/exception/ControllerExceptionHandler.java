package com.sergeykotov.op.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(value = AuthorizationException.class)
    public ResponseEntity<?> handleAuthorizationException(AuthorizationException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ResponseBody
    @ExceptionHandler(value = ModificationException.class)
    public ResponseEntity<?> handleModificationException(ModificationException exception) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("schedule is being generated, please wait to modify data");
    }

    @ResponseBody
    @ExceptionHandler(value = DatabaseException.class)
    public ResponseEntity<?> handleDatabaseException(DatabaseException exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("failed to modify the database: " + exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = ExtractionException.class)
    public ResponseEntity<?> handleExtractionException(ExtractionException exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("failed to extract data from the database: " + exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = InvalidDataException.class)
    public ResponseEntity<?> handleInvalidDataException(InvalidDataException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
}