package com.example.projektsklep.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = JwtSecurityTest.Probe.class, properties = {
        "security.jwt.secret=MDEyMzQ1Njc4OWFiY2RlZjAxMjM0NTY3ODlhYmNkZWY=",
        "security.jwt.cookie-secure=false"})
@Import({SecurityConfig.class, JwtTokens.class, JwtSecurityTest.Probe.class})
class JwtSecurityTest {
    @Autowired MockMvc mvc;
    @Autowired PasswordEncoder passwords;
    @MockBean UserDetailsService users;

    @RestController
    static class Probe {
        @GetMapping({"/account/test", "/admin/test", "/orders", "/users", "/swagger-ui/index.html"})
        String account(java.security.Principal principal) { return principal.getName(); }
    }

    private void user() {
        when(users.loadUserByUsername("user@example.com")).thenReturn(
                User.withUsername("user@example.com").password(passwords.encode("password")).authorities("USER").build());
    }

    @Test
    void loginAuthenticatesWithCookieWithoutSessionAndChecksRoles() throws Exception {
        user();
        var result = mvc.perform(post("/user/login").with(csrf())
                        .param("username", "user@example.com").param("password", "password"))
                .andExpect(redirectedUrl("/home")).andReturn();
        var cookie = result.getResponse().getCookie(JwtTokens.COOKIE);
        assertNotNull(cookie);
        assertTrue(cookie.isHttpOnly());
        assertNull(result.getRequest().getSession(false));
        mvc.perform(get("/account/test").cookie(cookie))
                .andExpect(status().isOk()).andExpect(content().string("user@example.com"));
        mvc.perform(get("/admin/test").cookie(cookie)).andExpect(status().isForbidden());
        mvc.perform(post("/logout").cookie(cookie).with(csrf()))
                .andExpect(redirectedUrl("/home")).andExpect(cookie().maxAge(JwtTokens.COOKIE, 0));
    }

    @Test
    void privateListingsAreUnavailableToGuestsAndOrdinaryUsers() throws Exception {
        user();
        var result = mvc.perform(post("/user/login").with(csrf())
                        .param("username", "user@example.com").param("password", "password")).andReturn();
        for (String path : java.util.List.of("/orders", "/users", "/swagger-ui/index.html")) {
            mvc.perform(get(path)).andExpect(status().is3xxRedirection());
            mvc.perform(get(path).cookie(result.getResponse().getCookie(JwtTokens.COOKIE)))
                    .andExpect(status().isForbidden());
        }
    }

    @Test
    void preservesBasketSessionWithoutStoringAuthentication() throws Exception {
        user();
        var session = new org.springframework.mock.web.MockHttpSession();
        session.setAttribute("basket", "existing-basket");
        mvc.perform(post("/user/login").session(session).with(csrf())
                        .param("username", "user@example.com").param("password", "password"))
                .andExpect(redirectedUrl("/home"));
        assertEquals("existing-basket", session.getAttribute("basket"));
        assertNull(session.getAttribute("SPRING_SECURITY_CONTEXT"));
        mvc.perform(get("/account/test").session(session)).andExpect(status().is3xxRedirection());
    }

    @Test
    void rejectsWrongPasswordInvalidTokenAndMissingCsrf() throws Exception {
        user();
        mvc.perform(post("/user/login").with(csrf()).param("username", "user@example.com")
                        .param("password", "wrong"))
                .andExpect(redirectedUrl("/user/login?error")).andExpect(cookie().doesNotExist(JwtTokens.COOKIE));
        mvc.perform(get("/account/test").cookie(new jakarta.servlet.http.Cookie(JwtTokens.COOKIE, "invalid")))
                .andExpect(status().is3xxRedirection()).andExpect(cookie().maxAge(JwtTokens.COOKIE, 0));
        mvc.perform(post("/user/login").param("username", "user@example.com").param("password", "password"))
                .andExpect(status().isForbidden());
    }

    @Test
    void verifiesSignatureExpiryAndSecretLength() {
        String secret = "MDEyMzQ1Njc4OWFiY2RlZjAxMjM0NTY3ODlhYmNkZWY=";
        var tokens = new JwtTokens(secret, 3600, true);
        var authentication = org.springframework.security.authentication.UsernamePasswordAuthenticationToken
                .authenticated("user@example.com", null, java.util.List.of());
        String token = tokens.issue(authentication);
        assertEquals("user@example.com", tokens.subject(token));
        var other = new JwtTokens("YWJjZGVmMDEyMzQ1Njc4OWFiY2RlZjAxMjM0NTY3ODk=", 3600, true);
        assertThrows(org.springframework.security.oauth2.jwt.JwtException.class, () -> other.subject(token));
        assertThrows(IllegalArgumentException.class, () -> new JwtTokens("c2hvcnQ=", 3600, true));
        var key = new javax.crypto.spec.SecretKeySpec(java.util.Base64.getDecoder().decode(secret), "HmacSHA256");
        var encoder = new org.springframework.security.oauth2.jwt.NimbusJwtEncoder(
                new com.nimbusds.jose.jwk.source.ImmutableSecret<>(key));
        var claims = org.springframework.security.oauth2.jwt.JwtClaimsSet.builder().issuer("big-piig-gastro")
                .subject("user@example.com").issuedAt(java.time.Instant.now().minusSeconds(200))
                .expiresAt(java.time.Instant.now().minusSeconds(120)).build();
        String expired = encoder.encode(org.springframework.security.oauth2.jwt.JwtEncoderParameters.from(
                org.springframework.security.oauth2.jwt.JwsHeader.with(
                        org.springframework.security.oauth2.jose.jws.MacAlgorithm.HS256).build(), claims)).getTokenValue();
        assertThrows(org.springframework.security.oauth2.jwt.JwtException.class, () -> tokens.subject(expired));
    }
}
