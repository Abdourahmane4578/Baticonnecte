package com.baticonnecte.baticonnecte.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import jakarta.annotation.PostConstruct;

@Service
public class JwtService {

    @Value("${JWT_SECRET:${jwt.secret:MaCleSecreteTresLonguePourJWT256BitsMinimum}}")
    private String secret;

    @Value("${JWT_EXPIRATION:${jwt.expiration:86400000}}")
    private long expiration;

    private Key key;

    @PostConstruct
    public void init() {
        if (secret == null || secret.length() < 32) {
            throw new IllegalStateException("La clé JWT doit faire au moins 32 caractères (256 bits)");
        }
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // ─── GÉNÉRATION ────────────────────────────────────────────────────────────────

    public String generateToken(UUID id, String userName, String email, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", id.toString());
        claims.put("nomComplet", userName);
        claims.put("email", email);
        claims.put("role", role);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    // ─── EXTRACTION ─────────────────────────────────────────────────────────────────

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public UUID extractUserId(String token) {
        String id = extractClaim(token, claims -> claims.get("id", String.class));
        return id != null ? UUID.fromString(id) : null;
    }

    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    public String extractFullName(String token) {
        return extractClaim(token, claims -> claims.get("nomComplet", String.class));
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // ✅ CORRIGÉ : Pas de .build() ici
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)  // ← Appel direct, sans .build()
                .getBody();
    }

    // ─── VALIDATION ─────────────────────────────────────────────────────────────────

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            return (username != null &&
                    username.equals(userDetails.getUsername()) &&
                    !isTokenExpired(token));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    // ✅ CORRIGÉ : Pas de .build() ici non plus
    public Claims validateToken(String token) throws ExpiredJwtException, SignatureException, MalformedJwtException {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)  // ← Appel direct, sans .build()
                .getBody();
    }

    public boolean validateTokenAndReturnStatus(String token) {
        try {
            validateToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // ─── UTILITAIRES ───────────────────────────────────────────────────────────────

    public long getExpirationTime() {
        return expiration;
    }

    public boolean isTokenExpiringSoon(String token, int minutesThreshold) {
        try {
            Date expiration = extractExpiration(token);
            Date threshold = new Date(System.currentTimeMillis() + (minutesThreshold * 60 * 1000L));
            return expiration.before(threshold);
        } catch (Exception e) {
            return true;
        }
    }
}