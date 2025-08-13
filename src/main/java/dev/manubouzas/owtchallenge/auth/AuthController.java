package dev.manubouzas.owtchallenge.auth;

import dev.manubouzas.owtchallenge.boat.Boat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.stream.Collectors;

/**
 * REST controller for authentication.
 * Handles JWT token generation for authenticated users.
 * Provides a single endpoint to generate a token based on user credentials.
 * This controller uses Spring Security's JwtEncoder to create JWT tokens
 *
 * @author Manuel
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    /**
     * JwtEncoder is used to encode JWT tokens.
     */
    private final JwtEncoder encoder;

    /**
     * Constructor to inject the JwtEncoder dependency.
     *
     * @param encoder the JwtEncoder used to create JWT tokens
     */
    public AuthController(JwtEncoder encoder) {
        this.encoder = encoder;
    }

    /**
     * Response object containing the JWT token.
     * This record is used to encapsulate the JWT token returned by the authentication endpoint.
     *
     * @param token the JWT token as a String
     */
    public static record JwtResponse(String token) {}

    /**
     * Authenticates a user and generates a JWT token.
     *
     * @param authentication the authentication object containing user credentials and authorities
     * @return ResponseEntity containing the JWT token
     */
    @PostMapping("")
    public ResponseEntity<JwtResponse> auth(Authentication authentication) {
        Instant now = Instant.now();
        long expiry = 36000L;
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("owtChallenge")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(authentication.getName())
                .claim("scope", scope)
                .build();
         return ResponseEntity.ok(new JwtResponse(this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue()));
    }



}
