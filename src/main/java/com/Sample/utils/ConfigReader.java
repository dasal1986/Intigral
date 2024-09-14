package com.Sample.utils;

import java.io.InputStream;
import java.util.Properties;
import java.util.Base64;

public class ConfigReader {
    private static final Properties prop = new Properties();

    static {
        // Load environment-specific properties file
        String environment = System.getProperty("environment");
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("config-" + environment + ".properties")) {
            if (input == null) {
                throw new RuntimeException("Sorry, unable to find config-" + environment + ".properties");
            } else {
                System.out.println("Loaded config-" + environment + ".properties");
            }
            prop.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Unable to load the properties file");
        }
    }

    public static String getUrl() {
        return prop.getProperty("url");
    }

    public static String getUsername() {
        return prop.getProperty("username");
    }

    public static String getCaseUrls() {
        return prop.getProperty("case.urls");
    }

    public static int getLoopIterationCount() {
        return Integer.parseInt(prop.getProperty("loop.iteration.count"));
    }

    public static String getName() {
        return prop.getProperty("name");
    }

    public static String getPassword() {
        // Decode Base64 encoded password
        return new String(Base64.getDecoder().decode(prop.getProperty("password")));
    }

    public static String getSqlUsername() {
        return prop.getProperty("sqlusername");
    }

    public static String getSqlPassword() {
        // Decode Base64 encoded password
        return new String(Base64.getDecoder().decode(prop.getProperty("sqlpassword")));
    }

    public static String getServerName() {
        return prop.getProperty("servername");
    }

    public static String getSourceDirectory() {
        return prop.getProperty("source.directory");
    }

    public static String getDestinationDirectory() {
        return prop.getProperty("destination.directory");
    }
}

