package kz.bitlab.courseplatform.mapper;

import kz.bitlab.courseplatform.dto.CourseCreateRequest;
import kz.bitlab.courseplatform.dto.CourseResponse;
import kz.bitlab.courseplatform.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "updatedTime", ignore = true)
    Course toEntity (CourseCreateRequest request);

    CourseResponse toResponse(Course course);


}
