package com.davit.libraryapi.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    private final Object[] args;

    public EmailAlreadyExistsException(String message) {
        super(message);
        this.args = null;
    }

    public EmailAlreadyExistsException(String message, Object... args) {
        super(message);
        this.args = args;
    }

    public Object[] getArgs() {
        return args;
    }
}
