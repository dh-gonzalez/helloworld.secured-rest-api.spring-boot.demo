package helloworld.secured_rest_api.spring_boot.demo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import helloworld.secured_rest_api.spring_boot.demo.common.Constants;
import helloworld.secured_rest_api.spring_boot.demo.service.TokenService;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * Authentication web filter which :<br>
 * - extract token from HTTP header Authorization<br>
 * - decode user with its authorities from token<br>
 * - and write user/authorities within Security Context
 */
public class JwtTokenAuthenticationFilter implements WebFilter {

    /** Logger for this class */
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    /** Token service */
    private final TokenService _tokenService;

    /**
     * Constructor
     * 
     * @param tokenService token service
     */
    public JwtTokenAuthenticationFilter(TokenService tokenService) {
        _tokenService = tokenService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String token = resolveToken(exchange.getRequest());
        if (StringUtils.hasText(token) && _tokenService.validateToken(token)) {
            return Mono.fromCallable(() -> _tokenService.getAuthentication(token))
                    .subscribeOn(Schedulers.boundedElastic())
                    .flatMap(authentication -> chain.filter(exchange).contextWrite(
                            ReactiveSecurityContextHolder.withAuthentication(authentication)));
        }
        return chain.filter(exchange);
    }

    /**
     * Retrieve token from HTTP header request
     * 
     * @param request HTTP request
     * @return token
     */
    private String resolveToken(ServerHttpRequest request) {
        String bearerToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(Constants.KEY_BEARER)
                && bearerToken.length() > 7) {
            // "Bearer token" the token starts at index 7
            String token = bearerToken.substring(7);
            LOGGER.debug("Token has been extracted from Authorization HTTP header");
            return token;
        }
        return null;
    }
}
