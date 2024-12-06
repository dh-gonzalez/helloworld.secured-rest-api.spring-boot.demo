package helloworld.secured_rest_api.spring_boot.demo.service;

import org.springframework.security.core.Authentication;

/**
 * Token service
 */
public interface TokenService {

    /**
     * Generate a token for authenticated user
     * 
     * @param authentication authenticated user
     * @return token
     */
    String generateToken(Authentication authentication);

    /**
     * Retrieve authentication from token
     * 
     * @param token token
     * @return authentication from token
     */
    Authentication getAuthentication(String token);

    /**
     * Validate token
     * 
     * @param token token
     * @return true if token is valid
     */
    boolean validateToken(String token);
}
