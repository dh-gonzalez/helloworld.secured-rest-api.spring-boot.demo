package helloworld.secured_rest_api.spring_boot.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import helloworld.secured_rest_api.spring_boot.demo.common.Constants;
import helloworld.secured_rest_api.spring_boot.demo.service.TokenService;

/**
 * Security configuration
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public ReactiveUserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User.withUsername("admin").password(passwordEncoder.encode("admin"))
                .authorities(Constants.authorityAdministrator).build();
        return new MapReactiveUserDetailsService(user);
    }

    @Bean
    public ReactiveAuthenticationManager authenticationManager(PasswordEncoder passwordEncoder,
            ReactiveUserDetailsService userDetailsService) {
        UserDetailsRepositoryReactiveAuthenticationManager authenticationManager =
                new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
        authenticationManager.setPasswordEncoder(passwordEncoder);
        return authenticationManager;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,
            TokenService tokenService, ReactiveAuthenticationManager authenticationManager) {

        return http
                //
                .authenticationManager(authenticationManager)
                // TODO - Manage CSRF
                .csrf(csrf -> csrf.disable())
                // Manage authorisations
                .authorizeExchange(exchanges -> exchanges
                        // Everybody can access to Swagger UI
                        .pathMatchers(
                                // JSON API documentation endpoint
                                "/v3/api-docs/**",
                                // Swagger UI
                                "/swagger-ui.html", "webjars/swagger-ui/**")
                        .permitAll()
                        // Everybody can access liveness/readyness probes
                        .pathMatchers("/actuator", "/actuator/**").permitAll()
                        // Everybody can request authentication
                        .pathMatchers(Constants.api + Constants.v1 + "/authenticate").permitAll()
                        // Everybody can request non secured endpoints
                        .pathMatchers(Constants.api + Constants.v1 + "/anonymous/**").permitAll()
                        // Only administrator can request secured endpoints
                        .pathMatchers(Constants.api + Constants.v1 + "/secured/**")
                        .hasAuthority(Constants.authorityAdministrator)
                        // any other request requires the user to be authenticated
                        .anyExchange().authenticated())
                //
                .addFilterAt(new JwtTokenAuthenticationFilter(tokenService),
                        SecurityWebFiltersOrder.HTTP_BASIC)
                //
                .build();
    }
}
