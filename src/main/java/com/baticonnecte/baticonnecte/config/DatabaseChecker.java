package com.baticonnecte.baticonnecte.config;

import jakarta.annotation.PostConstruct;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseChecker {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseChecker(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void checkConnection() {
        try {
            jdbcTemplate.execute("SELECT 1");
            System.out.println("✅ Base de données connectée avec succès!");
        } catch (Exception e) {
            System.err.println("❌ Erreur de connexion à PostgreSQL : " + e.getMessage());
        }
    }

}
