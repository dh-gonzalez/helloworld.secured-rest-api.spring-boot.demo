package helloworld.secured_rest_api.spring_boot.demo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;
import helloworld.secured_rest_api.spring_boot.demo.common.Constants;
import helloworld.secured_rest_api.spring_boot.demo.dto.AuthenticationRequestDTO;
import helloworld.secured_rest_api.spring_boot.demo.dto.TokenDTO;
import helloworld.secured_rest_api.spring_boot.demo.service.TokenService;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * Unit test class of controller AuthenticationControllerTest
 */
@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerTest {

    // The services to be mocked
    @Mock
    private TokenService tokenService;

    @Mock
    private ReactiveAuthenticationManager authenticationManager;

    // The controller to be tested
    @InjectMocks
    private AuthenticationController authenticationController;

    @Test
    public void test_Authenticate_Nominal() {

        ReflectionTestUtils.setField(authenticationController, "_jwtExpiration", 3600);

        // Test data
        Authentication authentication =
                new UsernamePasswordAuthenticationToken("admin", "admin", Collections.singletonList(
                        new SimpleGrantedAuthority(Constants.AUTHORITY_ADMINISTRATOR)));
        AuthenticationRequestDTO authenticationRequestDTO = new AuthenticationRequestDTO();
        authenticationRequestDTO.setUsername("admin");
        authenticationRequestDTO.setPassword("admin");
        TokenDTO tokenDTO = new TokenDTO("token", "Bearer", 3600);

        // Mocks behaviour
        when(authenticationManager.authenticate(any())).thenReturn(Mono.just(authentication));
        when(tokenService.generateToken(any())).thenReturn("token");

        // Call service method to test
        Mono<TokenDTO> response =
                authenticationController.authenticate(Mono.just(authenticationRequestDTO));
        // Retrieve asynchronous result and check expected result
        StepVerifier.create(response).consumeNextWith(retrievedToken -> {
            assertNotNull(retrievedToken);
            assertEquals(tokenDTO.getToken(), retrievedToken.getToken());
            assertEquals(tokenDTO.getType(), retrievedToken.getType());
            assertEquals(tokenDTO.getExpiresInSeconds(), retrievedToken.getExpiresInSeconds());
        }).verifyComplete();

        // Check mocks have been correctly called
        verify(authenticationManager).authenticate(any());
        verify(tokenService).generateToken(any());
    }
}
