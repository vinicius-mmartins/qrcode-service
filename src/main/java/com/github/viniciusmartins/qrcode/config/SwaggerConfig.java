package com.github.viniciusmartins.qrcode.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("qrcode-service")
                        .description("API responsável pelo cadastro de QRCode")
                        .version("1.0.0")
                );
    }

}
