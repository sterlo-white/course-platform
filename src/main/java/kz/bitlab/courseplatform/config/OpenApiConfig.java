package kz.bitlab.courseplatform.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Course Platform API",
                version = "1.0",
                description = "REST API for managing courses, chapters and lessons"
        )
)
public class OpenApiConfig {
}