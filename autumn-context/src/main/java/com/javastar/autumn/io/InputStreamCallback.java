package com.javastar.autumn.io;

import java.io.InputStream;

@FunctionalInterface
public interface InputStreamCallback<T> {

    T doWithInputStream(InputStream inputStream);
}
