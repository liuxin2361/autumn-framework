package com.javastar.autumn.exception;

public class BeanNotOfRequiredTypeException extends BeansException{
    public BeanNotOfRequiredTypeException() {}

    public BeanNotOfRequiredTypeException(String msg) {
        super(msg);
    }
}
