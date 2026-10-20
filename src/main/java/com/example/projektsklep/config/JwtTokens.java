package com.example.projektsklep.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;

@Component
public class JwtTokens {
    public static final String COOKIE = "ACCESS_TOKEN";
    private static final String ISSUER = "big-piig-gastro";
    private final JwtEncoder encoder;
    private final JwtDecoder decoder;
    private final Duration lifetime;
    private final boolean secure;

    public JwtTokens(@Value("${security.jwt.secret}") String secret,
                     @Value("${security.jwt.ttl-seconds:3600}") long ttl,
                     @Value("${security.jwt.cookie-secure:true}") boolean secure) {
        byte[] bytes = Base64.getDecoder().decode(secret);
        if (bytes.length < 32 || ttl <= 0) {
            throw new IllegalArgumentException("JWT requires a Base64 secret of at least 32 bytes and a positive TTL");
        }
        var key = new SecretKeySpec(bytes, "HmacSHA256");
        encoder = new NimbusJwtEncoder(new ImmutableSecret<>(key));
        var jwtDecoder = NimbusJwtDecoder.withSecretKey(key).macAlgorithm(MacAlgorithm.HS256).build();
        jwtDecoder.setJwtValidator(JwtValidators.createDefaultWithIssuer(ISSUER));
        decoder = jwtDecoder;
        lifetime = Duration.ofSeconds(ttl);
        this.secure = secure;
    }

    public String issue(Authentication authentication) {
        Instant now = Instant.now();
        var claims = JwtClaimsSet.builder().issuer(ISSUER).subject(authentication.getName())
                .issuedAt(now).expiresAt(now.plus(lifetime)).build();
        return encoder.encode(JwtEncoderParameters.from(
                JwsHeader.with(MacAlgorithm.HS256).build(), claims)).getTokenValue();
    }

    public String subject(String token) {
        return decoder.decode(token).getSubject();
    }

    public void writeCookie(HttpServletRequest request, HttpServletResponse response, String token) {
        response.addHeader(HttpHeaders.SET_COOKIE, ResponseCookie.from(COOKIE, token)
                .httpOnly(true).secure(secure || request.isSecure()).sameSite("Lax").path("/")
                .maxAge(token.isEmpty() ? Duration.ZERO : lifetime).build().toString());
    }
}
