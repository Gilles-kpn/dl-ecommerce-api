package fr.gilles.auth.docs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "E-commerce api",
                version = "1.0",
                description = "Using Module for authentication",
                contact = @Contact(
                        name = "KPANOU Gilles",
                        url = "http://localhost:8080",
                        email = "okpanou2@gmail.com"
                )

        )
)
@SecurityScheme(
        name = "Bearer Token",
        scheme = "JWT",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.HEADER,
        paramName = "Authorization",
        description = "Bearer token in header"
)

@EnableWebMvc
public class SwaggerDoc {

}
