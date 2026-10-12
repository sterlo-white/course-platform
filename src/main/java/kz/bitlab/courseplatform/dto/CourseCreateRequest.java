package kz.bitlab.courseplatform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Request for creating or updating a course")

public class CourseCreateRequest {

    @NotBlank(message = "Name is required")
    @Schema( description = "Course name", example = "Java Backend Development", requiredMode = Schema.RequiredMode.REQUIRED )
    private String name;
    private String description;

}
