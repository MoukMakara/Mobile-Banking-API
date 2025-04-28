package co.istad.mbanking.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${mbanking.openapi.dev-url}")
    private String devUrl;

    @Value("${mbanking.openapi.stage-url}")
    private String stageUrl;

    @Value("${mbanking.openapi.prod-url}")
    private String prodUrl;

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }

    @Bean
    public OpenAPI openAPI() {

        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Server URL in Development environment");

        Server stageServer = new Server();
        stageServer.setUrl(stageUrl);
        stageServer.setDescription("Server URL in Testing environment");

        Server prodServer = new Server();
        prodServer.setUrl(prodUrl);
        prodServer.setDescription("Server URL in Production environment");


        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement()
                        .addList("Bearer Authentication")
                )
                .components(new Components().addSecuritySchemes
                        ("Bearer Authentication", createAPIKeyScheme()))
                .info(new Info()
                        .title("SBC11 Mobile Banking API")
                        .description("SBC11 Mobile Banking API 2024")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Chan Chhaya")
                                .email("it.chhaya@gmail.com")
                                .url("www.istad.co")
                        )
                        .license(new License().name("License of API")
                                .url("API license URL")
                        )
                )
                .servers(List.of(devServer, stageServer, prodServer));
    }

}
