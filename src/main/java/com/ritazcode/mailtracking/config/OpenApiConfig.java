package com.ritazcode.mailtracking.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * config for open api swagger
 */
@Configuration
public class OpenApiConfig {
    public class OpenAPIConfig {

        @Value("${myConfig.openapi.server-url}")
        private String url;

        @Bean
        public OpenAPI socialMediaAPI() {
            Server myServer = new Server();
            myServer.setUrl(url);
            myServer.setDescription("localhost URL");

            Contact contact = new Contact();
            contact.setEmail("rita@gmail.com");
            contact.setName("ritazcode");
            contact.setUrl("https://www.ritazcode.com");

            Info info = new Info()
                    .title("Mail Tracking API")
                    .version("1.0")
                    .contact(contact)
                    .description("This is a mail tracking api");

            return new OpenAPI().info(info).servers(List.of(myServer));
        }

    }
}
