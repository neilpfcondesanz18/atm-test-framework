package com.atm.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Central configuration manager — reads from environment variables first, then config files.
 */
public class FrameworkConfig {

    private static final Logger log = LoggerFactory.getLogger(FrameworkConfig.class);
    private static final Properties props = new Properties();
    private static FrameworkConfig instance;

    private FrameworkConfig() {
        String env = System.getProperty("test.env", System.getenv().getOrDefault("TEST_ENV", "local"));
        loadProperties("config/framework.properties");
        loadProperties("config/" + env + ".properties");
        log.info("Framework config loaded for environment: {}", env);
    }

    public static FrameworkConfig getInstance() {
        if (instance == null) {
            synchronized (FrameworkConfig.class) {
                if (instance == null) instance = new FrameworkConfig();
            }
        }
        return instance;
    }

    private void loadProperties(String path) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(path)) {
            if (is != null) {
                props.load(is);
                log.debug("Loaded properties from: {}", path);
            }
        } catch (IOException e) {
            log.warn("Could not load properties from: {}", path);
        }
    }

    public String get(String key) {
        // Environment variable takes highest priority (for CI/CD)
        String envKey = key.toUpperCase().replace(".", "_");
        String envVal = System.getenv(envKey);
        if (envVal != null) return envVal;
        return System.getProperty(key, props.getProperty(key, ""));
    }

    public String get(String key, String defaultValue) {
        String value = get(key);
        return value.isEmpty() ? defaultValue : value;
    }

    public int getInt(String key, int defaultValue) {
        try { return Integer.parseInt(get(key)); }
        catch (NumberFormatException e) { return defaultValue; }
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        String v = get(key);
        return v.isEmpty() ? defaultValue : Boolean.parseBoolean(v);
    }

    // Convenience accessors
    public String getBaseUrl() { return get("api.base.url", "http://localhost:8080"); }
    public String getBrowser() { return get("ui.browser", "chrome"); }
    public boolean isHeadless() { return getBoolean("ui.headless", true); }
    public int getApiTimeout() { return getInt("api.timeout.seconds", 30); }
    public int getUiTimeout() { return getInt("ui.timeout.seconds", 15); }
}
