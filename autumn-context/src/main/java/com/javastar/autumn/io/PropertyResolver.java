package com.javastar.autumn.io;

import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;

/**
 * ${app.title}
 * ${app.title:defaultValue}
 * ${app.title:${APP_NAME:Autumn}}
 * 上記のみつ型に対して解析する機能
 */
public class PropertyResolver {

    Logger logger = LoggerFactory.getLogger(getClass());

    Map<String, String> properties = new HashMap<>();
    Map<Class<?>, Function<String, Object>> converts = new HashMap<>();

    <T> T convert(Class<?> clazz, String value) {
        Function<String, Object> fn = this.converts.get(clazz);
        if (fn == null) {
            throw new IllegalArgumentException("Unsupported value type: " + clazz.getName());
        }
        return (T) fn.apply(value);
    }

    public PropertyResolver(Properties properties) {
        this.properties.putAll(System.getenv());
        Set<String> names = properties.stringPropertyNames();

        for(String name : names) {
            this.properties.put(name, properties.getProperty(name));
        }
    }

    @Nullable
    public String getProperty(String key) {
        PropertyExpr keyExpr = parsePropertyExpr(key);
        if (keyExpr != null) {
            if (keyExpr.defaultValue != null) {
                return getProperty(keyExpr.key, keyExpr.defaultValue);
            } else {
                return getRequiredProperty(keyExpr.key);
            }
        }
        String value = this.properties.get(key);
        if (value != null) {
            return parseValue(value);
        }
        return value;
    }

    @Nullable
    public <T> T getProperty(String key, Class<T> targetType) {
        String value = getProperty(key);
        if (value == null) {
            return null;
        }
        return convert(targetType, value);
    }

    private String parseValue(String value) {
        PropertyExpr expr = parsePropertyExpr(value);
        if (expr == null) {
            return value;
        }
        if (expr.defaultValue != null) {
            return getProperty(expr.key, expr.defaultValue);
        } else {
            return getRequiredProperty(expr.key);
        }
    }

    public String getRequiredProperty(String key) {
        String value = getProperty(key);
        return Objects.requireNonNull(value, "Property '" + key + "' not found.");
    }

    public String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return value == null? parseValue(defaultValue) : value;
    }

    PropertyExpr parsePropertyExpr(String key) {
        if (key.startsWith("${") && key.endsWith("}")) {
            int n = key.indexOf(":");
            if (n == -1) {
                String k = key.substring(2, key.length() - 1);
                return new PropertyExpr(k, null);
            } else {
                String k = key.substring(2, n);
                return new PropertyExpr(k, key.substring(n + 1, key.length() - 1));
            }
        }
        return null;
    }

    record PropertyExpr(String key, String defaultValue) {}
}
