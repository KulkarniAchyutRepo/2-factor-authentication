package com.app._FactorAuthentication.config;

import com.app._FactorAuthentication.service.JwtService;
import com.app._FactorAuthentication.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private JwtService jwtService;
    private UserService userService;
    @Autowired
    public JwtAuthenticationFilter(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if(authHeader!=null && authHeader.startsWith("Bearer ")){
            String token = authHeader.substring(7);
            if(jwtService.validateToken(token)){
                String userName = jwtService.getUsernameFromToken(token);
                 UserDetails userDetails = userService.loadUserByUsername(userName);
                 if(SecurityContextHolder.getContext().getAuthentication()==null){
                     UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                             = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                     SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                 }
            }
        }
        filterChain.doFilter(request, response);
    }
}
