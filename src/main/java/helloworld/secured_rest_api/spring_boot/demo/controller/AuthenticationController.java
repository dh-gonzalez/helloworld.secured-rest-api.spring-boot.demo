package helloworld.secured_rest_api.spring_boot.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import helloworld.secured_rest_api.spring_boot.demo.common.Constants;
import helloworld.secured_rest_api.spring_boot.demo.dto.AuthenticationRequestDTO;
import helloworld.secured_rest_api.spring_boot.demo.dto.ErrorDetailsDTO;
import helloworld.secured_rest_api.spring_boot.demo.dto.TokenDTO;
import helloworld.secured_rest_api.spring_boot.demo.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import reactor.core.publisher.Mono;

/**
 * Controller which manages authentication
 */
@RestController
@RequestMapping(Constants.api)
public class AuthenticationController {

    /** Logger for this class */
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Value("${jwt.expiration:3600}")
    private long _jwtExpiration;

    /** Token service */
    private final TokenService _tokenService;

    /** Authentication manager */
    private final ReactiveAuthenticationManager _authenticationManager;

    /**
     * Constructor
     * 
     * @param tokenService token service
     * @param authenticationManager authentication manager
     */
    public AuthenticationController(TokenService tokenService,
            ReactiveAuthenticationManager authenticationManager) {
        _tokenService = tokenService;
        _authenticationManager = authenticationManager;
    }

    @Operation(summary = "User authentication",
            description = "Authenticate user with given credentials and provide a token in return")
    @ApiResponses({
            //
            @ApiResponse(responseCode = "200",
                    description = "User has been authenticated and a token has been provided"),
            //
            @ApiResponse(responseCode = "401", description = "Unauthorized access",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDetailsDTO.class))),
            //
            @ApiResponse(responseCode = "500",
                    description = "Unexpected error has occurred while processing request",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDetailsDTO.class)))})
    @PostMapping(value = Constants.v1 + "/authenticate",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<TokenDTO> authenticate(
            @RequestBody Mono<AuthenticationRequestDTO> authenticationRequest) {

        LOGGER.info("Authentication has been requested");

        return authenticationRequest.flatMap(
                // Authenticate user
                credentialsDTO -> _authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(
                                credentialsDTO.getUsername(), credentialsDTO.getPassword()))
                        // Generate token for user
                        .map(_tokenService::generateToken))
                // Map to DTO for caller
                .map(token -> new TokenDTO(token, Constants.KEY_BEARER, _jwtExpiration));
    }
}
