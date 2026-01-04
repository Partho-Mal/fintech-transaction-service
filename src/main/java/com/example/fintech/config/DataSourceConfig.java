//Datasource config
//Let Spring Boot handle pooling

package com.example.fintech.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {
    // Intentionally empty.
    // Spring Boot autoconfigures HikariCP.
}

