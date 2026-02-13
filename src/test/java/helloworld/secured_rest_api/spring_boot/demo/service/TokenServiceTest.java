package helloworld.secured_rest_api.spring_boot.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Collection;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;
import helloworld.secured_rest_api.spring_boot.demo.common.Constants;
import helloworld.secured_rest_api.spring_boot.demo.service.impl.TokenServiceImpl;

/**
 * Unit test class of service TokenService
 */
@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {

    // The service to be tested
    @InjectMocks
    private TokenServiceImpl tokenService;

    @BeforeEach
    public void init() {
        ReflectionTestUtils.setField(tokenService, "_jwtSecret",
                "S3cr3tK3y4JWT-HS512_uSE$Th1s-K3y-FoR-SecureT0k3nSignatur3");
        ReflectionTestUtils.setField(tokenService, "_jwtExpiration", 3600);
        tokenService.init();
    }

    @Test
    public void test_generateToken_Nominal() {

        // Test nominal case
        Authentication authentication =
                new UsernamePasswordAuthenticationToken("admin", "admin", Collections.singletonList(
                        new SimpleGrantedAuthority(Constants.AUTHORITY_ADMINISTRATOR)));
        String token = tokenService.generateToken(authentication);
        assertNotNull(token);
    }

    @Test
    public void test_getAuthentication_Nominal() {

        // Test nominal case with authorities
        Authentication authentication =
                new UsernamePasswordAuthenticationToken("admin", "admin", Collections.singletonList(
                        new SimpleGrantedAuthority(Constants.AUTHORITY_ADMINISTRATOR)));
        String token = tokenService.generateToken(authentication);
        assertNotNull(token);

        // Check authentication from token
        Authentication retrievedAuthentication = tokenService.getAuthentication(token);
        assertNotNull(retrievedAuthentication);
        assertEquals("admin", retrievedAuthentication.getPrincipal());
        assertEquals("", retrievedAuthentication.getCredentials());
        Collection<? extends GrantedAuthority> retrievedAuthorities =
                retrievedAuthentication.getAuthorities();
        assertNotNull(retrievedAuthorities);
        assertEquals(1, retrievedAuthorities.size());
        GrantedAuthority retrievedAuthority = retrievedAuthorities.iterator().next();
        assertEquals(Constants.AUTHORITY_ADMINISTRATOR, retrievedAuthority.getAuthority());

        // Test nominal case with no authorities
        authentication = new UsernamePasswordAuthenticationToken("admin", "admin");
        token = tokenService.generateToken(authentication);
        assertNotNull(token);

        // Check authentication from token
        retrievedAuthentication = tokenService.getAuthentication(token);
        assertNotNull(retrievedAuthentication);
        assertEquals("admin", retrievedAuthentication.getPrincipal());
        assertEquals("", retrievedAuthentication.getCredentials());
        retrievedAuthorities = retrievedAuthentication.getAuthorities();
        assertNotNull(retrievedAuthorities);
        assertEquals(0, retrievedAuthorities.size());
    }

    @Test
    public void test_validateToken_Nominal() {

        // Test nominal case
        Authentication authentication =
                new UsernamePasswordAuthenticationToken("admin", "admin", Collections.singletonList(
                        new SimpleGrantedAuthority(Constants.AUTHORITY_ADMINISTRATOR)));
        String token = tokenService.generateToken(authentication);
        assertNotNull(token);

        // Check token validity
        assertTrue(tokenService.validateToken(token));
    }

    @Test
    public void test_validateToken_expiredToken() {

        // Set negative expiration in order to generate expired token
        ReflectionTestUtils.setField(tokenService, "_jwtExpiration", -3600);

        Authentication authentication =
                new UsernamePasswordAuthenticationToken("admin", "admin", Collections.singletonList(
                        new SimpleGrantedAuthority(Constants.AUTHORITY_ADMINISTRATOR)));
        String token = tokenService.generateToken(authentication);
        assertNotNull(token);

        // Test token is expired
        assertFalse(tokenService.validateToken(token));
    }

    @Test
    public void test_validateToken_Exceptions() {

        // Test token is null
        assertFalse(tokenService.validateToken(null));

        // Test token is invalid
        assertFalse(tokenService.validateToken("wrongToken"));
    }
}
