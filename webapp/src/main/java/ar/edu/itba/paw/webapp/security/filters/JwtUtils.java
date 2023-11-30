package ar.edu.itba.paw.webapp.security.filters;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {
    private static final long REFRESH_EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7;
    private static final long EXPIRATION_TIME = 1000 * 60 * 10;

    @Autowired
    private Environment environment;

    public String generateToken(String email) {
        final Claims claims = Jwts.claims()
                .subject(email)
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .build();

        return Jwts.builder()
                .claims(claims)
                .signWith(getSigningKey())
                .compact();
    }

    public String generateRefreshToken(String email) {
        final Claims claims = Jwts.claims()
                .subject(email)
                .expiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION_TIME))
                .add("refresh", true)
                .build();

        return Jwts.builder()
                .claims(claims)
                .signWith(getSigningKey())
                .compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getEmail(String token) {
        return getClaims(token).getSubject();
    }

    public boolean isRefreshToken(String token) {
        return getClaims(token).get("refresh", Boolean.class) != null;
    }

    public boolean validate(String token) {
        try {
            Jwts.parser().verifyWith(getSigningKey()).build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    private SecretKey getSigningKey() {
        String secret = environment.getRequiredProperty("jwt.secret");
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}