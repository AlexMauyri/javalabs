package ru.ssau.tk.DoubleA.javalabs.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {
    private String secretkey = "7d85e89aaa1d4a8fbc6d36c38e80a3c0ba0bffb5c5775f83f25ad8f24a08fca859be7505b4ef539e749da239a56637f61dc576d18e108def1a43d0f6274726d52b71450945f5227540b49bad749f69738072603d51623efd039fc3b7542763150ae864e03ccc2af6b849821fa0ce9c8be24ec1e78aca980497c615f2072dc2c6b015452885eaa01145d73581ca54309d6003ab3ed95b8a54d43ee6739cb6aee8d1c7a208a80562cccd0c55fbb9471f2f2cf3d6c431dd297a3a7e9564d27a91f3dc5d79ee4f84d2c835ef3effc179ac55d7c04fdc63375a4bd6a6563f5f7085838c4c4fe0a08663291ac11166063bf8d5aeb7726c3595f583c1e6cd3e5dd5b902";


    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .and()
                .signWith(getKey())
                .compact();

    }

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretkey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return userName.equals(userDetails.getUsername());
    }
}
