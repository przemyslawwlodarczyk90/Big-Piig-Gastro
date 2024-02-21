package com.example.projektsklep.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Klasa konfiguracyjna do ustawień bezpieczeństwa aplikacji.
 */
@Configuration
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Definiuje bean PasswordEncoder, który dostarcza mechanizm do kodowania i weryfikacji haseł.
     *
     * @return instancja BCryptPasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Konfiguruje dostawcę uwierzytelniania za pomocą DaoAuthenticationProvider, który korzysta z UserDetailsService
     * do pobierania informacji o użytkownikach oraz PasswordEncoder do kodowania haseł.
     *
     * @return skonfigurowany bean AuthenticationProvider.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    /**
     * Definiuje główny łańcuch filtrów bezpieczeństwa aplikacji. Określa zasady dostępu do różnych części aplikacji,
     * konfigurację logowania i wylogowywania, oraz dodatkowe ustawienia bezpieczeństwa jak CSRF i CORS.
     *
     * @param http obiekt HttpSecurity do konfiguracji zabezpieczeń.
     * @return skonfigurowany łańcuch filtrów bezpieczeństwa.
     * @throws Exception w przypadku błędów konfiguracji.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(new AntPathRequestMatcher("/home")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/api/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/static/")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/users")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/categories")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/orders/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/admin/**")).hasAuthority("ADMIN")
                        .requestMatchers(new AntPathRequestMatcher("/account/**")).hasAuthority("USER")
                        .requestMatchers(new AntPathRequestMatcher("/user/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("api/productDetails")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/users/new")).permitAll()
                        .anyRequest().authenticated())
                .formLogin((form) -> form
                        .loginPage("/user/login")
                        .loginProcessingUrl("/user/login")
                        .defaultSuccessUrl("/home", true)
                        .permitAll())
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/home")
                        .permitAll())
                .httpBasic(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));

        return http.build();
    }
}