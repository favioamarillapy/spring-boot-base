package com.py.konecta.base.service.impl;

import com.py.konecta.base.service.JwtService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Service
@Slf4j
public class JwtServiceImpl implements JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String jwtSigningKey;

    @Value("${application.security.jwt.expiration}")
    private int expiration;

    @Override
    public String extractUserName(String token, HttpServletRequest request) {
        return extractClaim(token, Claims::getSubject, request);
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .claims(new HashMap<>())
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails, HttpServletRequest request) {
        final String userName = extractUserName(token, request);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token, request);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers, HttpServletRequest request) {
        try {
            final Claims claims = extractAllClaims(token, request);
            return claimsResolvers.apply(claims);
        } catch (Exception ex) {
            log.error("Error to extract claims: ", ex);
        }
        return null;
    }

    private boolean isTokenExpired(String token, HttpServletRequest request) {
        return extractExpiration(token, request).before(new Date());
    }

    private Date extractExpiration(String token, HttpServletRequest request) {
        return extractClaim(token, Claims::getExpiration, request);
    }

    private Claims extractAllClaims(String token, HttpServletRequest request) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (MalformedJwtException ex) {
            log.error("Sesion invalida", ex);
            request.setAttribute("expired", "Sesion invalida");
        } catch (ExpiredJwtException ex) {
            log.error("Su sesion ha expirado", ex);
            request.setAttribute("expired", "Su sesion ha expirado");
        } catch (UnsupportedJwtException ex) {
            log.error("Sesion no soportada", ex);
            request.setAttribute("expired", "Sesion no soportada");
        } catch (IllegalArgumentException ex) {
            log.error("No se encontro la sesion", ex);
            request.setAttribute("expired", "No se encontro la sesion");
        }

        return null;
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSigningKey.getBytes(StandardCharsets.UTF_8));
    }
}
