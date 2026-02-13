package helloworld.secured_rest_api.spring_boot.demo.service.impl;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;
import helloworld.secured_rest_api.spring_boot.demo.common.Constants;
import helloworld.secured_rest_api.spring_boot.demo.service.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

/**
 * JWT token service implementation
 */
@Service
public class TokenServiceImpl implements TokenService {

    /** Logger for this class */
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Value("${jwt.secret}")
    private String _jwtSecret;

    @Value("${jwt.expiration:3600}")
    private long _jwtExpiration;

    /** Secret key */
    private SecretKey _secretKey;

    @PostConstruct
    public void init() {
        // Initialization of secret key in order to encode and decode token
        String secret = Base64.getEncoder().encodeToString(_jwtSecret.getBytes());
        _secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String generateToken(Authentication authentication) {

        // Retrieve username and authorities
        String username = authentication.getName();
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

        LOGGER.debug("Token generation has been requested for user {} with authorities {}",
                username, authorities);

        Claims claims =
                Jwts.claims().subject(username).add(Constants.KEY_AUTHORITIES, authorities).build();

        return Jwts.builder()
                //
                .claims(claims)
                //
                .issuedAt(new Date())
                //
                .expiration(new Date(System.currentTimeMillis() + _jwtExpiration * 1000))
                //
                .signWith(_secretKey, Jwts.SIG.HS512)
                //
                .compact();
    }

    @Override
    public Authentication getAuthentication(String token) {

        Claims claims =
                Jwts.parser().verifyWith(_secretKey).build().parseSignedClaims(token).getPayload();

        Object authoritiesClaim = claims.get(Constants.KEY_AUTHORITIES);

        Collection<? extends GrantedAuthority> authorities =
                AuthorityUtils.commaSeparatedStringToAuthorityList(authoritiesClaim.toString());

        LOGGER.debug("Extracted data from token - user {} with authorities {}", claims.getSubject(),
                authorities);

        return new UsernamePasswordAuthenticationToken(claims.getSubject(), "", authorities);
    }

    @Override
    public boolean validateToken(String token) {
        try {
            // No need to check explicitly token expiration date as the method parseSignedClaims
            // will do it and throw an ExpiredJwtException in case token is expired
            Jws<Claims> claims =
                    Jwts.parser().verifyWith(_secretKey).build().parseSignedClaims(token);
            LOGGER.debug("Token is valid - expiration date: {}",
                    claims.getPayload().getExpiration());
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            LOGGER.debug("Invalid JWT token: {}", e.getMessage());
        }
        return false;
    }
}
