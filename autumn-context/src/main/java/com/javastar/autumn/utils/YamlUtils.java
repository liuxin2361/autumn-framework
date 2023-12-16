package com.javastar.autumn.utils;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;
import org.yaml.snakeyaml.resolver.Resolver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <a href="https://github.com/snakeyaml/snakeyaml">Parse yaml by snakeyaml</a>
 */
public class YamlUtils {

    public static Map<String, Object> loadYaml(String path) {
        var loaderOptions = new LoaderOptions();
        var dumperOptions = new DumperOptions();
        var resolver = new NoImplicitResolver();
        var yaml = new Yaml(new Constructor(loaderOptions), new Representer(dumperOptions), new DumperOptions(), resolver);
        return ClassPathUtils.readInputStream(path, yaml::load);
    }

    public static Map<String, Object> loadYamlAsPlainMap(String path) {
        Map<String, Object> data = loadYaml(path);
        Map<String, Object> plainMap = new HashMap<>();
        convertTo(data, "", plainMap);
        return plainMap;
    }

    static void convertTo(Map<String, Object> sourceMap, String prefix, Map<String, Object> plainMap) {
        for(String key : sourceMap.keySet()) {
            Object value = sourceMap.get(key);
            if (value instanceof Map<?,?>) {
                Map<String, Object> subMap = (Map<String, Object>) value;
                convertTo(subMap, prefix + key + ".", plainMap);
            } else if (value instanceof List<?>) {
                plainMap.put(prefix + key, value);
            } else {
                plainMap.put(prefix + key, value.toString());
            }
        }
    }
}

/**
 * Disable ALL implicit convert and treat all values as string.
 */
class NoImplicitResolver extends Resolver {

    public NoImplicitResolver() {
        super();
        super.yamlImplicitResolvers.clear();
    }
}
