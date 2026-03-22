package com.baticonnecte.baticonnecte.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseChecker {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DatabaseChecker(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void checkConnection() {
        try {
            jdbcTemplate.execute("SELECT 1");
            System.out.println("✅ PostgreSQL est connecté !");
        } catch (Exception e) {
            System.err.println("❌ Erreur de connexion à PostgreSQL : " + e.getMessage());
        }
    }

}
