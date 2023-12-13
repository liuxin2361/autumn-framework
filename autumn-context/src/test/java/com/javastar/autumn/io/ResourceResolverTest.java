package com.javastar.autumn.io;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ResourceResolverTest {

    @Test
    public void scanClass() {
        var pkg = "com.javastar.autumn";
        var resourceResolver = new ResourceResolver(pkg);
        List<String> classes = resourceResolver.scan(res -> {
            String name = res.name();
            if(name.endsWith(".class")) {
               return name.substring(0, name.length() - 6).replace("/", ".").replace("\\", ".");
            }
            return null;
        });
        System.out.println(classes);
    }

    @Test
    public void scanTxt() {
        var pkg = "com.javastar.autumn";
        var resourceResolver = new ResourceResolver(pkg);
        List<String> classes = resourceResolver.scan(res -> {
            String name = res.name();
            if(name.endsWith(".txt")) {
                return name.replace("\\", "/");
            }
            return null;
        });
        Collections.sort(classes);
        assertArrayEquals(new String[] {
                // txt files:
                "com/javastar/autumn/scan/sub/sub1Test.txt",
                "com/javastar/autumn/scan/subTest.txt"
        }, classes.toArray(String[]::new));
    }
}