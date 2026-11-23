package org.gl.authservice.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.gl.authservice.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    private SecretKey secretKey(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(User user) {
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("role",user.getRole().name())
                .claim("email", user.getEmail())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60  * 15))
                .signWith(secretKey())
                .compact();
    }

}
