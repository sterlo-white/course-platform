package kz.bitlab.courseplatform.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChapterResponse {
    private Long id;
    private String name;
    private String description;
    private Integer order;
    private Long courseId;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}

