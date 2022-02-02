package com.juan.market.web.security.filter;

import com.juan.market.domain.service.JuanUserDetailsService;
import com.juan.market.web.security.JWTUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilterRequest extends OncePerRequestFilter {

    private JWTUtil jwtUtil;
    private JuanUserDetailsService juanUserDetailsService;

    public JwtFilterRequest(JWTUtil jwtUtil, JuanUserDetailsService juanUserDetailsService) {
        this.jwtUtil = jwtUtil;
        this.juanUserDetailsService = juanUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer")){
            String jwt = authorizationHeader.substring(7);
            String userName = jwtUtil.extracUserName(jwt);

            if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails = juanUserDetailsService.loadUserByUsername(userName);

                if(jwtUtil.validateToken(jwt, userDetails)){
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails,
                                    null,
                                    userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
