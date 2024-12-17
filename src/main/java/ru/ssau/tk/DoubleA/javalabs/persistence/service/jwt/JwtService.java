package ru.ssau.tk.DoubleA.javalabs.persistence.service.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private final Key key = Keys.hmacShaKeyFor("G?WTkr_E.KMve`DYL-$'~qQUt]);>hyX6CjB3c,S(ZA&{%8fR".getBytes());

    public String generateToken(Long userId, String username) {
        return Jwts.builder()
                .claim("userId", userId)
                .claim("username", username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 2)) // 2 дня
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims parseToken(String jwt) {
        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }
}
