package com.javastar.autumn.exception;

public class NestedRuntimeException extends RuntimeException{

    public NestedRuntimeException() {}

    public NestedRuntimeException(String message) {
        super(message);
    }

    public NestedRuntimeException(Throwable cause) {
        super(cause);
    }

    public NestedRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
