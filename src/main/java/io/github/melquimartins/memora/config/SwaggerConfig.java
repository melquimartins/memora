package io.github.melquimartins.memora.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI configOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Memora")
                .description(String.join(
                        " ",
                        "Está API RESTful faz parte do projeto **Memora**, um sistema de",
                        "gerenciamento de estudo através de desafios."
                ))
        );
    }

}
