package com.example.Datn_clean.Service;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Controller
@Service
public class JWTAuthFilter extends OncePerRequestFilter {
    @Autowired
    JWTService jwtService;
    @Autowired
    UserDetailsService userDetailsService;

    @Override protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse respone,
            FilterChain filterchain
    ) throws ServletException, IOException{
        if(SecurityContextHolder.getContext().getAuthentication() == null) {
            String authorization = request.getHeader("Authorization");
            if (authorization != null && authorization.startsWith("Bearer ")) {
                String token = authorization.substring(7).trim();
                try {
                    var claims = jwtService.parseToken(token);
                    if (jwtService.validate(claims)) {
                        String username = claims.getSubject();
                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                        var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
        filterchain.doFilter(request, respone);
    }
}
