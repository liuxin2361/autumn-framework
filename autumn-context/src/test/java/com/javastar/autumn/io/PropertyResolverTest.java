package com.javastar.autumn.io;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class PropertyResolverTest {

    @Test
    public void propertyValue() {
        var props = new Properties();

        props.setProperty("app.title", "Autumn Framework");
        props.setProperty("app.version", "1.0-SNAPSHOT");
        props.setProperty("jdbc.url", "jdbc:mysql://localhost:3306/autumn");
        props.setProperty("jdbc.username", "root");
        props.setProperty("jdbc.password", "12qw34er");
        props.setProperty("jdbc.pool-size", "50");
        props.setProperty("jdbc.auto-commit", "true");

        var pr = new PropertyResolver(props);
        assertEquals("Autumn Framework", pr.getProperty("app.title"));
        assertEquals("1.0-SNAPSHOT", pr.getProperty("app.version"));
        assertEquals("50", pr.getProperty("jdbc.pool-size", "20"));

        assertNull(pr.getProperty("app.author"));
        assertEquals("Han MeiMei", pr.getProperty("app.author", "Han MeiMei"));

        assertTrue(pr.getProperty("jdbc.auto-commit", boolean.class));
        assertTrue(pr.getProperty("jdbc.auto-commit", Boolean.class));
        assertTrue(pr.getProperty("jdbc.detect-leak", true, boolean.class));
    }

    @Test
    public void requiredProperty() {
        var props = new Properties();

        props.setProperty("app.title", "Autumn Framework");
        props.setProperty("app.version", "1.0-SNAPSHOT");

        var pr = new PropertyResolver(props);
        assertThrows(NullPointerException.class, () -> pr.getRequiredProperty("not.exist"));
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    public void propertyHolderOnWin() {
        String os = System.getenv("OS");
        System.out.println("env OS=" + os);

        String homePath = System.getenv("HOMEPATH");

        var props = new Properties();
        props.setProperty("app.title", "Autumn Framework");

        var pr = new PropertyResolver(props);
        assertEquals("Windows_NT", pr.getProperty("${app.os:${OS}}"));
        assertEquals(homePath, pr.getProperty("${app.path:${HOMEPATH}}"));
        assertEquals("/not-exist", pr.getProperty("${app.path:${app.home:${HOME_NOT_EXIST:/not-exist}}}"));

        assertEquals("Autumn Framework", pr.getProperty("${app.title}"));

        assertThrows(NullPointerException.class, () -> {
            pr.getProperty("${app.version}");
        });

        assertEquals("v1.0", pr.getProperty("${app.version:v1.0}"));
        assertEquals(1, pr.getProperty("${app.version:1}", int.class));
        assertThrows(NumberFormatException.class, () -> {
            pr.getProperty("${app.version:x}", int.class);
        });
    }
}