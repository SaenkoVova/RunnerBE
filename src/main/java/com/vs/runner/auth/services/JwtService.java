package com.vs.runner.auth.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private static final String SECRET_KEY = "2qNAYa1NDHM6HgwpQhVIvsBLzYlZPxJ8PFF+6K2rNFAYJ0G1seiQiU8mRW/atJBEWBYRlZgAsvMAEfxtVN/HwwSvlSk/sRPl+KOiZbAPGyFsKvCGRMMG7rfnAHqBN3wqWuakwQCD2s6wQ1PkGqBCOjXq+X5snn779G+l2gxZz0rlAWCpiNtEjeS4hUwGoQrRYxK8MWReMyEsMe8aYbBJNoelQ2gyWoNl0Q0yJpB07lzCS/5wEVV3q57DBNefHArgNHXtAdOUffI2dSclxp3sSqFNx8cJUFimn8pZ8b0VcytHlHBTmQ6p3O9tAFfBnVzrt7QRfCPT9TZqB7jNJsTBNOVWC+Gy9GUjkZdNoA3Xpuw=";
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSigningKey())
                .compact();

    }
    
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
