package com.authorization.springauthorization.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JWTUtils {

    private static final long EXPIRY_DATE = 1000*60*30;

    private final String SECRET = "Very-secret-key-unlock-0r-hack-the-application-f0r-tim3-taken@90908762312";

    private final SecretKey key  = Keys.hmacShaKeyFor(SECRET.getBytes());

    public  String generateToken(String username){

       return Jwts.builder()

               //payload
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRY_DATE))

               //signature
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    public String getUserNameFromToken(String token){
        return extractorMethod(token).getSubject();
    }

    private Claims extractorMethod(String token){
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public boolean validateToken(String username, String username1, String token) {
        return username.equals(username1) && !isTokenValid(token) ;
    }

    private boolean isTokenValid(String token) {
        return extractorMethod(token).getExpiration().before(new Date());
    }
}
