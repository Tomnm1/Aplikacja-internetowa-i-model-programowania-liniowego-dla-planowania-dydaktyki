package pl.poznan.put;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.filter.OncePerRequestFilter;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class JwtVerifierFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtVerifierFilter.class);

    private final RSAPublicKey publicKey;
    private final String issuer;

    public JwtVerifierFilter(RSAPublicKey publicKey, String issuer) {
        this.publicKey = publicKey;
        this.issuer = issuer;
        logger.info("JwtVerifierFilter initialized with issuer: {}", issuer);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            logger.debug("Received Bearer token: {}", token.substring(0, Math.min(token.length(), 30)) + "...");

            try {
                logger.debug("Attempting to verify JWT token.");
                DecodedJWT decodedHeaderOnly = JWT.decode(token);
                logger.debug("Token claims issuer: {}", decodedHeaderOnly.getIssuer());
                logger.debug("Expected issuer: {}", issuer);

                Algorithm algorithm = Algorithm.RSA256(publicKey, null);

                var verifier = JWT.require(algorithm)
                        .withIssuer(issuer)
                        .acceptLeeway (7200)
                        .build();

                DecodedJWT decoded = verifier.verify(token);

                logger.debug("JWT token successfully verified. Subject: {}", decoded.getSubject());

                Jwt springJwt = convertDecodedJWTToSpringJwt(decoded);

                var authentication = new org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken(springJwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                logger.debug("Authentication set in SecurityContextHolder.");
            }
            catch (JWTVerificationException ex) {
                logger.warn("JWT verification failed: {}", ex.getMessage());
                SecurityContextHolder.clearContext();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid JWT token: " + ex.getMessage());
                return;
            }
            catch (Exception e) {
                logger.error("Unexpected error during JWT processing: {}", e.getMessage(), e);
                SecurityContextHolder.clearContext();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Error reading JWT: " + e.getMessage());
                return;
            }
        } else {
            logger.debug("No Bearer token found in Authorization header.");
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Metoda konwertuje DecodedJWT (Auth0) -> Jwt (Spring Security),
     * tak by dalej AdminCheckFilter mógł się spodziewać principal = Jwt.
     */
    private Jwt convertDecodedJWTToSpringJwt(DecodedJWT decoded) throws IOException {
        String headerJson = new String(Base64.getUrlDecoder().decode(decoded.getHeader()));

        Map<String, Object> headers = new ObjectMapper ().readValue(
                headerJson,
                new TypeReference<> () {
                }
        );

        logger.debug("JWT Headers: {}", headerJson);

        Map<String, Object> claims = new HashMap<>();
        decoded.getClaims().forEach((k, v) -> {
            Object claimValue = v.as(Object.class);
            claims.put(k, claimValue);
            logger.debug("JWT Claim - {}: {}", k, claimValue);
        });

        Instant issuedAt  = decoded.getIssuedAt()  == null ? Instant.EPOCH : decoded.getIssuedAt().toInstant();
        Instant expiresAt = decoded.getExpiresAt() == null ? Instant.EPOCH : decoded.getExpiresAt().toInstant();

        logger.debug("JWT Issued At: {}", issuedAt);
        logger.debug("JWT Expires At: {}", expiresAt);

        return new Jwt(decoded.getToken(), issuedAt, expiresAt, headers, claims);
    }
}
