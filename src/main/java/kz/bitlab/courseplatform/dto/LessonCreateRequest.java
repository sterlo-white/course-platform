package kz.bitlab.courseplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LessonCreateRequest {
    @NotBlank
    private String name;
    private String description;
    private String content;

    @NotNull
    private Integer order;

    @NotNull
    private Long chapterId;
}
