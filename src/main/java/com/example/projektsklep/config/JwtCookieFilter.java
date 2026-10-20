package com.example.projektsklep.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtCookieFilter extends OncePerRequestFilter {
    private final JwtTokens tokens;
    private final UserDetailsService users;

    public JwtCookieFilter(JwtTokens tokens, UserDetailsService users) {
        this.tokens = tokens;
        this.users = users;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        if (request.getCookies() != null) {
            for (var cookie : request.getCookies()) {
                if (!JwtTokens.COOKIE.equals(cookie.getName())) continue;
                try {
                    var user = users.loadUserByUsername(tokens.subject(cookie.getValue()));
                    new AccountStatusUserDetailsChecker().check(user);
                    var context = SecurityContextHolder.createEmptyContext();
                    context.setAuthentication(UsernamePasswordAuthenticationToken.authenticated(
                            user, null, user.getAuthorities()));
                    SecurityContextHolder.setContext(context);
                } catch (JwtException | AuthenticationException | IllegalArgumentException ex) {
                    SecurityContextHolder.clearContext();
                    tokens.writeCookie(request, response, "");
                }
                break;
            }
        }
        chain.doFilter(request, response);
    }
}
