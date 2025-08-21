package com.example.utkarshbackend.jwt;

import com.example.utkarshbackend.dto.AuthUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret-key}")
    private String secretKey;

    public Long extractUserId(String jwtToken) {
        Integer userIdInt = extractClaims(jwtToken, claims -> claims.get("userId", Integer.class));
        return userIdInt != null ? userIdInt.longValue() : null;
    }

    private <T> T extractClaims (String jwtToken, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(jwtToken);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims (String jwtToken) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();
    }

    private SecretKey getSignInKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generateToken(AuthUser user, long expiration) {

        return generateToken(new HashMap<>(), user, expiration);
    }

    public String generateToken(Map<String, Object> claims, AuthUser user, long expiration) {
        claims.put("userId", user.getId());

        return Jwts.builder()
                .claims(claims)
                .subject(user.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey())
                .compact();
    }

    public String extractEmail(String jwtToken) {
//         return extractClaims(jwtToken, claims -> claims.getSubject());
        return extractClaims(jwtToken, Claims::getSubject);
    }

    public boolean isTokenValid(String jwtToken, AuthUser user) {
        final Long userId = extractUserId(jwtToken);
        return (userId != null && userId.equals((user.getId()))) && !isTokenExpired(jwtToken);
    }

    public boolean isTokenValid(String jwtToken, UserDetails userDetails) {
        final String email = extractEmail(jwtToken);
        return (email.equals(userDetails.getUsername())) && !isTokenExpired(jwtToken);
    }

    private boolean isTokenExpired(String jwtToken) {
        return extractExpiration(jwtToken).before(new Date());
    }

    private Date extractExpiration(String jwtToken) {
        return extractClaims(jwtToken, Claims::getExpiration);
    }
}
