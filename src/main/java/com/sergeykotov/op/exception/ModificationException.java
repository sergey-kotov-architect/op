package com.sergeykotov.op.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "schedule is being generated, please wait to modify data")
public class ModificationException extends RuntimeException {
}