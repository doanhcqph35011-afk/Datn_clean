package com.example.datn_meta.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
@Controller
@Service
public class JWTService {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    private Key getSigningKey(){
        // Khai báo chuỗi khóa ( sinh tự động hoặc dùng tool )
        String secretKey = "secretKey";
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        // tạo khóa bằng thuật toán hmac
        return Keys.hmacShaKeyFor(keyBytes);
    }
    // tạo chuỗi jwt từ thông tin người dùng và thời gian hiệu lực
    public String createToken (UserDetails userDetails, int expirySeconds) {
        // lấy thời gian hiệnj tại theo ms
        long now = System.currentTimeMillis();
        Claims claims = Jwts.claims().setSubject(userDetails.getUsername());
        claims.put("roles", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());

        return Jwts.builder().setClaims(claims)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expirySeconds * 1000L))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    // phân tích payload của token
    public Claims parseToken(String token){
        return Jwts.parserBuilder()
                // cung cấp khóa xác minh chữ ký tự của tooken
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJwt(token)
                .getBody();
    }
    // kiểm tra thời gian hết hạn
    public boolean validate(Claims claims){
        return claims.getExpiration().after(new Date());
    }
}
