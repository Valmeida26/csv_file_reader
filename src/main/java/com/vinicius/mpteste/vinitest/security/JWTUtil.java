package com.vinicius.mpteste.vinitest.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Objects;

@Component
public class JWTUtil {

    //Pega o valor direto da application.properties
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    //Metodo para gerar o token
    public String generateToken(String username){

        SecretKey key = getKeyBySecret();
        return Jwts.builder().setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + this.expiration))
                .signWith(key).compact();
    }

    //Metodo para gerar a key
    private SecretKey getKeyBySecret(){
        return Keys.hmacShaKeyFor(this.secret.getBytes());
    }

    //Verifica se o token e valido
    public boolean isValidToken(String token) {
        Claims claims = getClaims(token);
        if (Objects.nonNull(claims)) {
            String username = claims.getSubject();
            Date expirationDate = claims.getExpiration();
            Date now = new Date(System.currentTimeMillis());
            if (Objects.nonNull(username) && Objects.nonNull(expirationDate) && now.before(expirationDate))
                return true;
        }
        return false;
    }

    public String getUsername(String token){
        Claims claims = getClaims(token);
        if (Objects.nonNull(claims)){
            return claims.getSubject();
        }
        return null;
    }

    //Decrita o token
    private Claims getClaims(String token){
        SecretKey key = getKeyBySecret();
        try {
            return Jwts.parser().setSigningKey(key).build().parseClaimsJws(token).getBody();
        }catch (Exception e){
            return null;
        }
    }

}
