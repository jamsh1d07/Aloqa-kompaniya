package com.example.aloqa_kompaniya.security;

import com.example.aloqa_kompaniya.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        token = token.substring(7);
        if (jwtProvider.validatetoken(token)) {
            if (jwtProvider.expireToken(token)) {
                String username = jwtProvider.getUserNameFromToken(token);
                UserDetails userDetails = authService.loadUserByUsername(username);
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities())
                );
                System.out.println(SecurityContextHolder.getContext().getAuthentication());
            }
        }
        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        doFilter(request, response, filterChain);
    }
}
