package com.example.schoolregistrationsystem.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "School registration system REST API", version = "1.0",
        description = "Documentation of application that allows students to enroll on courses.<br /> " +
                "Following information are for CRUD operations for student and course endpoint and " +
                "for information about whole service on endpoint /service"))
public class SwaggerConfig {
    @Bean
    public OpenApiCustomiser openApiCustomiser() {
        return openApi -> {
            var HashMap = new ObjectSchema()
                    .name("Map")
                    .title("Map")
                    .description("Map with field name as key, and value")
                    .addProperties("fieldName", new StringSchema().example("value"));

            var schemas = openApi.getComponents().getSchemas();
            schemas.put(HashMap.getName(), HashMap);
        };
    }

}
