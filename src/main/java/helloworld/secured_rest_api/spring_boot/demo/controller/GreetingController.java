package helloworld.secured_rest_api.spring_boot.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import helloworld.secured_rest_api.spring_boot.demo.common.Constants;
import helloworld.secured_rest_api.spring_boot.demo.dto.ErrorDetailsDTO;
import helloworld.secured_rest_api.spring_boot.demo.dto.GreetingDTO;
import helloworld.secured_rest_api.spring_boot.demo.service.GreetingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import reactor.core.publisher.Mono;

/**
 * This is an example of REST controller layer
 */
@RestController
@RequestMapping(Constants.api)
public class GreetingController {

    /** Logger for this class */
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    /** Greeting service */
    private final GreetingService _greetingService;

    /**
     * Constructor
     * 
     * @param greetingService Greeting service
     */
    public GreetingController(GreetingService greetingService) {
        _greetingService = greetingService;
    }

    @Operation(summary = "Return an anonymous greeting message",
            description = "Return 'Hello, World!' greeting message")
    @ApiResponses({
            //
            @ApiResponse(responseCode = "200", description = "Greeting message has been returned"),
            //
            @ApiResponse(responseCode = "500",
                    description = "Unexpected error has occurred while processing request",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDetailsDTO.class)))})
    @GetMapping(value = Constants.v1 + "/anonymous/greeting",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<GreetingDTO> greeting() {

        LOGGER.info("Non secured greeting has been requested");

        return Mono.just(_greetingService.greeting());
    }

    @Operation(summary = "Return a secured greeting message",
            description = "Return 'Hello, {name}!' greeting message")
    @ApiResponses({
            //
            @ApiResponse(responseCode = "200", description = "Greeting message has been returned"),
            //
            @ApiResponse(responseCode = "401", description = "Unauthorized access",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDetailsDTO.class))),
            //
            @ApiResponse(responseCode = "500",
                    description = "Unexpected error has occurred while processing request",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDetailsDTO.class)))})
    @SecurityRequirement(name = "bearerAuthentication")
    @GetMapping(value = Constants.v1 + "/secured/greeting",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<GreetingDTO> greeting(@Parameter(description = "name",
            example = "David") @RequestParam(value = "name") String name) {

        LOGGER.info("Secured greeting has been requested for {}", name);

        return Mono.just(_greetingService.greeting(name));
    }
}
