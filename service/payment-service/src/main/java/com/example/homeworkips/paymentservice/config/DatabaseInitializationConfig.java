package com.example.homeworkips.paymentservice.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseInitializationConfig implements ApplicationRunner {

    private final DatabaseClient databaseClient;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Initializing database schema and data...");
        executeScript("schema.sql");
        log.info("Database initialization completed.");
    }

    private void executeScript(String scriptPath) {
        try {
            ClassPathResource resource = new ClassPathResource(scriptPath);
            String script = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);

            String[] statements = script.split(";");
            
            for (String statement : statements) {
                String trimmedStatement = statement.trim();
                if (!trimmedStatement.isEmpty() && !trimmedStatement.startsWith("--")) {
                    databaseClient.sql(trimmedStatement)
                            .fetch()
                            .rowsUpdated()
                            .block();
                }
            }
            
            log.info("Successfully executed script: {}", scriptPath);
        } catch (Exception e) {
            log.error("Failed to execute script: {}", scriptPath, e);
            throw new RuntimeException("Database initialization failed", e);
        }
    }
}
