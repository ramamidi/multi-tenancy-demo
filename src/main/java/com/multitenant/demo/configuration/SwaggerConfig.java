package com.multitenant.demo.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Multi Tenant Demo Application")
                        .version("1.0")
                        .description("Multi-Tenant demo application with multi tenancy at the DB level(Separate DB for each tenant)"));
    }

    @Bean
    public OpenApiCustomizer customGlobalHeaderOpenApiCustomizer() {
        return openApi -> openApi.getPaths().forEach(
                (path, pathItem) -> pathItem.readOperations().forEach(
                        operation -> {
                            if (!path.startsWith("/api/1.0/tenant")) {
                                operation.addParametersItem(new Parameter()
                                        .in("header").name("X-TenantId")
                                        .required(true)
                                        .description("A tenant Id as custom header must be sent to all endpoints")
                                        .example(new String("tenantX"))
                                        .schema(new StringSchema()));
                            }
                        }));
    }
}
