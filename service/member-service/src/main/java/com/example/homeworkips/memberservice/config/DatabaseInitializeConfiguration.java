package com.example.homeworkips.memberservice.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(1) // Redis 초기화보다 먼저 실행
public class DatabaseInitializeConfiguration implements ApplicationRunner {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Initializing member database schema and data...");
        executeScript("schema.sql");
        executeScript("data.sql");
        log.info("Member database initialization completed.");
    }

    private void executeScript(String scriptPath) {
        try {
            ClassPathResource resource = new ClassPathResource(scriptPath);
            String script = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);

            String[] statements = script.split(";");
            
            for (String statement : statements) {
                String trimmedStatement = statement.trim();
                if (!trimmedStatement.isEmpty() && !trimmedStatement.startsWith("--")) {
                    jdbcTemplate.execute(trimmedStatement);
                }
            }
            
            log.info("Successfully executed member service script: {}", scriptPath);
        } catch (Exception e) {
            log.error("Failed to execute member service script: {}", scriptPath, e);
            throw new RuntimeException("Member database initialization failed", e);
        }
    }
}
