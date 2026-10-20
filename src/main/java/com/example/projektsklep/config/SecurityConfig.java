package com.example.projektsklep.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.context.NullSecurityContextRepository;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {


    private UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtTokens tokens) throws Exception {
        http
                .securityContext(context -> context.securityContextRepository(new NullSecurityContextRepository()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.NEVER))
                .requestCache(cache -> cache.disable())
                .addFilterBefore(new JwtCookieFilter(tokens, userDetailsService), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/users/new", "/users/registrationSuccess").permitAll()
                        .requestMatchers("/orders/create").hasAuthority("USER")
                        .requestMatchers("/admin/**", "/orders/**", "/orders", "/users/**", "/users",
                                "/swagger-ui/**", "/v3/api-docs/**").hasAuthority("ADMIN")
                        .requestMatchers("/account/**").hasAuthority("USER")
                        .requestMatchers("/**").permitAll())
                .formLogin((form) -> form
                        .loginPage("/user/login")
                        .loginProcessingUrl("/user/login")
                        .successHandler((request, response, authentication) -> {
                            tokens.writeCookie(request, response, tokens.issue(authentication));
                            response.sendRedirect(request.getContextPath() + "/home");
                        })
                        .permitAll())
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .addLogoutHandler((request, response, authentication) -> tokens.writeCookie(request, response, ""))
                        .logoutSuccessUrl("/home")
                        .permitAll())
                .cors(cors -> cors.disable())
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));

        return http.build();
    }
}
