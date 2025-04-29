package com.israelopeters.ReadingNow_Backend.exception;

public class BookPostNotFoundException extends RuntimeException {
    public BookPostNotFoundException(String message) {
        super(message);
    }
}
