package com.app._FactorAuthentication.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {
    private static final String JWT_SECRET_KEY = "Kx92X#nB!!2jLxoPEgQs67FA8cPmA@vXJpT1FtGs9nI2";
    private static final long ACCESS_TOKEN_EXPIRATION = 15*60*1000; // 15 minutes
    private static final long REFRESH_TOKEN_EXPIRATION = 24*60*60*1000; // 24 hrs

    //generate a new token
    public String generateToken(String username, boolean isAccessToken){
        long expirationTime = isAccessToken?ACCESS_TOKEN_EXPIRATION:REFRESH_TOKEN_EXPIRATION;
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+expirationTime))
                .signWith(Keys.hmacShaKeyFor(JWT_SECRET_KEY.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    //get username from token
    public String getUsernameFromToken(String token){
        return Jwts.parser()
                .setSigningKey(JWT_SECRET_KEY.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    //validate token
    public boolean validateToken(String token){
        try{
            Jwts.parser()
                    .setSigningKey(JWT_SECRET_KEY.getBytes())
                    .build()
                    .parseClaimsJws(token);

            return true;
        }catch (Exception e){
            return false;
        }
    }
}
