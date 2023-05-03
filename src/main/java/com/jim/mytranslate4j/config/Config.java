package com.jim.mytranslate4j.config;

import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jim
 */
@Component
public class Config {
    private static final String CONFIG_FILE = "src/main/resources/config.yml";
    private static Map<String, Object> config;

    static {
        loadConfig();
    }

    public static void loadConfig() {
        Yaml yaml = new Yaml();
        Path configPath = Paths.get(CONFIG_FILE);

        if (Files.exists(configPath)) {
            try (InputStream inputStream = new FileInputStream(configPath.toFile())) {
                config = yaml.load(inputStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            config = new HashMap<>();
        }
    }

    public void saveConfig() {
        Yaml yaml = new Yaml();
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            yaml.dump(config, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object get(String key) {
        return config.get(key);
    }

    public static void set(String key, Object value) {
        config.put(key, value);
    }
}
