package kz.bitlab.courseplatform.service;

import kz.bitlab.courseplatform.dto.CourseCreateRequest;
import kz.bitlab.courseplatform.dto.CourseResponse;
import kz.bitlab.courseplatform.entity.Course;
import kz.bitlab.courseplatform.exception.ResourceNotFoundException;
import kz.bitlab.courseplatform.mapper.CourseMapper;
import kz.bitlab.courseplatform.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor

public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    public List<CourseResponse> getAll(){
        log.debug("Fetching all courses");
        List <CourseResponse> courses = courseRepository.findAll()
                .stream()
                .map(courseMapper::toResponse)
                .toList();
        log.debug("Successfully fetched {} courses", courses.size());
        return courses;
    }
    public CourseResponse getById(Long id){
        log.debug("Fething course with id: {}", id);

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> {
                        log.error("Course not found with id: {}", id);
                return new ResourceNotFoundException("Course not found with id: " + id);});
        return courseMapper.toResponse(course);
    }
    public CourseResponse create(CourseCreateRequest request){
        log.info("Creating new course with name: {}", request.getName());
        Course course = courseMapper.toEntity(request);

        Course savedCourse = courseRepository.save(course);
        log.info("Course successfully created with id {}", savedCourse.getId());
        return courseMapper.toResponse(savedCourse);
    }

    public CourseResponse update(Long id, CourseCreateRequest request) {
        log.info("UPdating course with id: {}", id);
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Course not found for update with id: {}", id);
                    return new ResourceNotFoundException("Course not found with id: " + id);
                });

        course.setName(request.getName());
        course.setDescription(request.getDescription());
        Course updatedCourse = courseRepository.save(course);
        log.info("Course successfully updated with id: {}",id);

        return courseMapper.toResponse(updatedCourse);
    }


    public  void delete (Long id){
        log.info("Deleting course with id; {}", id);
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Course not found for deletion with id: {}", id);
                    return new ResourceNotFoundException("Course not found with id: " + id);
                });
        courseRepository.delete(course);
        log.info("Course successfully deleted with id: {}", id);
    }
}
