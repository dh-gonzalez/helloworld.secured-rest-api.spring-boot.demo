package helloworld.secured_rest_api.spring_boot.demo.config;

import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "API Documentation", version = "1.0",
                description = "API Documentation"),
        security = @SecurityRequirement(name = "bearerAuth"))
@SecurityScheme(name = "bearerAuthentication", type = SecuritySchemeType.HTTP, scheme = "bearer",
        bearerFormat = "JWT")
public class OpenApiConfiguration {
    // Nothing to do
}