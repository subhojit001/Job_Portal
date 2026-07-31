package com.Subhojit.Hire_Hub.Security;

import com.Subhojit.Hire_Hub.Entity.User;
import com.Subhojit.Hire_Hub.Service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@RequiredArgsConstructor
@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
       final String requestHeader = request.getHeader("Authorization");
       if (requestHeader == null || !requestHeader.startsWith("Bearer ")){
           filterChain.doFilter(request,response);
           return;
       }

       String token = requestHeader.substring(7);
       String email = jwtUtil.extractEmail(token);
        System.out.println("Email from token: " + email);

       if(email!=null && SecurityContextHolder.getContext().getAuthentication()==null){
           UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
           UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                   new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
           SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
       }
       filterChain.doFilter(request,response);
    }
}
