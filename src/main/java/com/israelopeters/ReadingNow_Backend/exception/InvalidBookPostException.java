package com.israelopeters.ReadingNow_Backend.exception;

public class InvalidBookPostException extends RuntimeException {
    public InvalidBookPostException(String message) {
        super(message);
    }
}
