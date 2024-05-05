package com.javastar.autumn.exception;

public class NoUniqueBeanDefinitionException extends BeanDefinitionException{
    public NoUniqueBeanDefinitionException() {
    }

    public NoUniqueBeanDefinitionException(String message) {
        super(message);
    }
}
