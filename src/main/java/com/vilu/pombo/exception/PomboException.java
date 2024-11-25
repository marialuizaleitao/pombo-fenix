package com.vilu.pombo.exception;

import org.springframework.http.HttpStatus;

public class PomboException extends Exception {
    public PomboException(String message, HttpStatus badRequest) {
        super(message);
    }
}
