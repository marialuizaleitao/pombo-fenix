package com.vilu.pombo.exeption;

import org.springframework.http.HttpStatus;

public class PomboException extends Exception {
    public PomboException(String message, HttpStatus badRequest) {
        super(message);
    }
}
