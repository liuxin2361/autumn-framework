package com.javastar.autumn.exception;

public class NoSuchBeanDefinitionException extends BeanDefinitionException{
    public NoSuchBeanDefinitionException() {}

    public NoSuchBeanDefinitionException(String msg) {
        super(msg);
    }
}
