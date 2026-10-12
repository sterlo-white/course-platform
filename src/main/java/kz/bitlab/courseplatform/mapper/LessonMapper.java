package kz.bitlab.courseplatform.mapper;

import kz.bitlab.courseplatform.dto.LessonCreateRequest;
import kz.bitlab.courseplatform.dto.LessonResponse;
import kz.bitlab.courseplatform.entity.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface LessonMapper {
    @Mapping(target = "chapter", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "updatedTime", ignore = true)
    Lesson toEntity(LessonCreateRequest request);

    @Mapping(target = "chapterId", source = "chapter.id")
    LessonResponse toResponse(Lesson lesson);

}
