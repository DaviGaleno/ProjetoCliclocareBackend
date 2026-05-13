package com.ciclocare.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.refresh.expiration}")
    private Long refreshExpiration;

    public String gerarToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return criarToken(claims, userDetails.getUsername(), expiration);
    }

    public String gerarRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return criarToken(claims, userDetails.getUsername(), refreshExpiration);
    }

    private String criarToken(Map<String, Object> claims, String subject, Long expirationTime) {
        Date agora = new Date();
        Date dataExpiracao = new Date(agora.getTime() + expirationTime);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(agora)
                .setExpiration(dataExpiracao)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extrairEmail(String token) {
        return extrairClaims(token).getSubject();
    }

    public Date extrairDataExpiracao(String token) {
        return extrairClaims(token).getExpiration();
    }

    private Claims extrairClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Boolean isTokenValido(String token, UserDetails userDetails) {
        String email = extrairEmail(token);
        return email.equals(userDetails.getUsername()) && !isTokenExpirado(token);
    }

    private Boolean isTokenExpirado(String token) {
        return extrairDataExpiracao(token).before(new Date());
    }
}
