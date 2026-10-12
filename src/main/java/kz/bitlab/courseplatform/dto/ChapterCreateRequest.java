package kz.bitlab.courseplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChapterCreateRequest {
    @NotBlank
    private String name;
    private String description;

    @NotNull
    @PositiveOrZero
    private Integer order;

    @NotNull
    private  Long courseId;
}
