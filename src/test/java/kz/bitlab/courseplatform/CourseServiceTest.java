package kz.bitlab.courseplatform;

import kz.bitlab.courseplatform.dto.CourseCreateRequest;
import kz.bitlab.courseplatform.dto.CourseResponse;
import kz.bitlab.courseplatform.entity.Course;
import kz.bitlab.courseplatform.exception.ResourceNotFoundException;
import kz.bitlab.courseplatform.mapper.CourseMapper;
import kz.bitlab.courseplatform.repository.CourseRepository;
import kz.bitlab.courseplatform.service.CourseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private CourseMapper courseMapper;

    @InjectMocks
    private CourseService courseService;


    @Test
    void getAll_shouldReturnAllCourses() {
        // given
        Course course1 = new Course();
        Course course2 = new Course();

        CourseResponse response1 = new CourseResponse();
        CourseResponse response2 = new CourseResponse();

        when(courseRepository.findAll())
                .thenReturn(List.of(course1, course2));

        when(courseMapper.toResponse(course1))
                .thenReturn(response1);

        when(courseMapper.toResponse(course2))
                .thenReturn(response2);

        // when
        List<CourseResponse> result = courseService.getAll();

        // then
        assertEquals(2, result.size());
        assertEquals(List.of(response1, response2), result);

        verify(courseRepository).findAll();
        verify(courseMapper).toResponse(course1);
        verify(courseMapper).toResponse(course2);
    }


    @Test
    void getById_shouldReturnCourse_whenCourseExists() {
        // given
        Long id = 1L;

        Course course = new Course();
        CourseResponse response = new CourseResponse();

        when(courseRepository.findById(id))
                .thenReturn(Optional.of(course));

        when(courseMapper.toResponse(course))
                .thenReturn(response);

        // when
        CourseResponse result = courseService.getById(id);

        // then
        assertEquals(response, result);

        verify(courseRepository).findById(id);
        verify(courseMapper).toResponse(course);
    }


    @Test
    void getById_shouldThrowException_whenCourseDoesNotExist() {
        // given
        Long id = 1L;

        when(courseRepository.findById(id))
                .thenReturn(Optional.empty());

        // when & then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> courseService.getById(id)
        );

        assertEquals(
                "Course not found with id: 1",
                exception.getMessage()
        );

        verify(courseRepository).findById(id);
        verifyNoInteractions(courseMapper);
    }


    @Test
    void create_shouldCreateCourse() {
        // given
        CourseCreateRequest request = new CourseCreateRequest();
        request.setName("Java Developer");
        request.setDescription("Java development course");

        Course course = new Course();
        Course savedCourse = new Course();
        CourseResponse response = new CourseResponse();

        when(courseMapper.toEntity(request))
                .thenReturn(course);

        when(courseRepository.save(course))
                .thenReturn(savedCourse);

        when(courseMapper.toResponse(savedCourse))
                .thenReturn(response);

        // when
        CourseResponse result = courseService.create(request);

        // then
        assertEquals(response, result);

        verify(courseMapper).toEntity(request);
        verify(courseRepository).save(course);
        verify(courseMapper).toResponse(savedCourse);
    }


    @Test
    void update_shouldUpdateCourse_whenCourseExists() {
        // given
        Long id = 1L;

        CourseCreateRequest request = new CourseCreateRequest();
        request.setName("Updated Java Developer");
        request.setDescription("Updated description");

        Course course = new Course();
        CourseResponse response = new CourseResponse();

        when(courseRepository.findById(id))
                .thenReturn(Optional.of(course));

        when(courseRepository.save(course))
                .thenReturn(course);

        when(courseMapper.toResponse(course))
                .thenReturn(response);

        // when
        CourseResponse result = courseService.update(id, request);

        // then
        assertEquals(response, result);

        assertEquals(
                "Updated Java Developer",
                course.getName()
        );

        assertEquals(
                "Updated description",
                course.getDescription()
        );

        verify(courseRepository).findById(id);
        verify(courseRepository).save(course);
        verify(courseMapper).toResponse(course);
    }


    @Test
    void update_shouldThrowException_whenCourseDoesNotExist() {
        // given
        Long id = 1L;

        CourseCreateRequest request = new CourseCreateRequest();
        request.setName("Updated course");

        when(courseRepository.findById(id))
                .thenReturn(Optional.empty());

        // when & then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> courseService.update(id, request)
        );

        assertEquals(
                "Course not found with id: 1",
                exception.getMessage()
        );

        verify(courseRepository).findById(id);
        verify(courseRepository, never()).save(any());
        verifyNoInteractions(courseMapper);
    }


    @Test
    void delete_shouldDeleteCourse_whenCourseExists() {
        // given
        Long id = 1L;

        Course course = new Course();

        when(courseRepository.findById(id))
                .thenReturn(Optional.of(course));

        // when
        courseService.delete(id);

        // then
        verify(courseRepository).findById(id);
        verify(courseRepository).delete(course);
    }


    @Test
    void delete_shouldThrowException_whenCourseDoesNotExist() {
        // given
        Long id = 1L;

        when(courseRepository.findById(id))
                .thenReturn(Optional.empty());

        // when & then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> courseService.delete(id)
        );

        assertEquals(
                "Course not found with id: 1",
                exception.getMessage()
        );

        verify(courseRepository).findById(id);
        verify(courseRepository, never()).delete(any());
    }


}