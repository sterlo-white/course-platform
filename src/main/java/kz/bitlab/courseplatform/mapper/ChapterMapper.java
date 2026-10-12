package kz.bitlab.courseplatform.mapper;

import kz.bitlab.courseplatform.dto.ChapterCreateRequest;
import kz.bitlab.courseplatform.dto.ChapterResponse;
import kz.bitlab.courseplatform.entity.Chapter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChapterMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "updatedTime", ignore = true)
    Chapter toEntity(ChapterCreateRequest request);

    @Mapping(target = "courseId", source = "course.id")
    ChapterResponse toResponse(Chapter chapter);
}
