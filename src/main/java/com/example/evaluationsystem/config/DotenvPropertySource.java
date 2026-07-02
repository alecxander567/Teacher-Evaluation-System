// src/main/java/com/example/evaluationsystem/config/DotenvPropertySource.java
package com.example.evaluationsystem.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

import java.util.HashMap;
import java.util.Map;

public class DotenvPropertySource implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        try {
            Dotenv dotenv = Dotenv.configure()
                    .directory("./")
                    .ignoreIfMissing()
                    .load();

            ConfigurableEnvironment environment = applicationContext.getEnvironment();
            MutablePropertySources propertySources = environment.getPropertySources();

            Map<String, Object> dotenvProperties = new HashMap<>();

            // Database variables
            String dbUrl = dotenv.get("DB_URL");
            String dbUsername = dotenv.get("DB_USERNAME");
            String dbPassword = dotenv.get("DB_PASSWORD");
            
            // JWT variables
            String jwtSecret = dotenv.get("JWT_SECRET");
            String jwtExpiration = dotenv.get("JWT_EXPIRATION");

            if (dbUrl != null) {
                dotenvProperties.put("DB_URL", dbUrl);
                System.out.println("DB_URL loaded");
            }
            if (dbUsername != null) {
                dotenvProperties.put("DB_USERNAME", dbUsername);
                System.out.println("DB_USERNAME loaded");
            }
            if (dbPassword != null) {
                dotenvProperties.put("DB_PASSWORD", dbPassword);
                System.out.println("DB_PASSWORD loaded");
            }
            
            if (jwtSecret != null) {
                dotenvProperties.put("JWT_SECRET", jwtSecret);
                System.out.println("JWT_SECRET loaded");
            } else {
                System.err.println("WARNING: JWT_SECRET not found in .env file!");
            }
            
            if (jwtExpiration != null) {
                dotenvProperties.put("JWT_EXPIRATION", jwtExpiration);
                System.out.println("JWT_EXPIRATION loaded");
            }

            if (!dotenvProperties.isEmpty()) {
                PropertySource<?> propertySource = new MapPropertySource("dotenv", dotenvProperties);
                propertySources.addFirst(propertySource);
                System.out.println(".env file loaded successfully");
            } else {
                System.out.println("No .env variables found");
            }

        } catch (Exception e) {
            System.err.println("Failed to load .env file: " + e.getMessage());
            System.err.println("Working directory: " + System.getProperty("user.dir"));
        }
    }
}