package com.javastar.autumn.utils;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class YamlUtilsTest {

    @Test
    public void testLoadYaml() {
        Map<String, Object> configs = YamlUtils.loadYaml("application.yml");
        for (String key: configs.keySet()) {
            Object value = configs.get(key);
            System.out.println(key + ": " + value + " (" + value.getClass() + ")");
        }
    }

    @Test
    public void testLoadYamlAsPlainMap() {
        Map<String, Object> configs = YamlUtils.loadYamlAsPlainMap("/application.yml");
        for (String key: configs.keySet()) {
            Object value = configs.get(key);
            System.out.println(key + ": " + value + " (" + value.getClass() + ")");
        }

        assertEquals("Autumn Framework", configs.get("app.title"));
        assertEquals("1.0.0", configs.get("app.version"));
        assertNull(configs.get("app.author"));
        assertEquals("${AUTO_COMMIT:false}", configs.get("autumn.datasource.auto-commit"));
    }

}