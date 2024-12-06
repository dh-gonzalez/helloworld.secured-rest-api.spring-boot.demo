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
 * Authentication filter
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
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(Constants.keyBearer)) {
            String token = bearerToken.substring(7);
            LOGGER.debug("Extracted token from Authorization HTTP header : {}", token);
            return token; // "Bearer token" the token starts at index 7
        }
        return null;
    }
}
