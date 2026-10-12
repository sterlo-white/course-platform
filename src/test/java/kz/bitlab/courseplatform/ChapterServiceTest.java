package kz.bitlab.courseplatform;

import kz.bitlab.courseplatform.dto.ChapterCreateRequest;
import kz.bitlab.courseplatform.dto.ChapterResponse;
import kz.bitlab.courseplatform.entity.Chapter;
import kz.bitlab.courseplatform.entity.Course;
import kz.bitlab.courseplatform.exception.ResourceNotFoundException;
import kz.bitlab.courseplatform.mapper.ChapterMapper;
import kz.bitlab.courseplatform.repository.ChapterRepository;
import kz.bitlab.courseplatform.repository.CourseRepository;
import kz.bitlab.courseplatform.service.ChapterService;
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
public class ChapterServiceTest {
    @Mock
    private ChapterRepository chapterRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private ChapterMapper chapterMapper;

    @InjectMocks
    private ChapterService chapterService;


    @Test
    void getAll_shouldReturnAllChapters() {
        // given
        Chapter chapter1 = new Chapter();
        Chapter chapter2 = new Chapter();

        ChapterResponse response1 = new ChapterResponse();
        ChapterResponse response2 = new ChapterResponse();

        when(chapterRepository.findAll())
                .thenReturn(List.of(chapter1, chapter2));

        when(chapterMapper.toResponse(chapter1))
                .thenReturn(response1);

        when(chapterMapper.toResponse(chapter2))
                .thenReturn(response2);

        // when
        List<ChapterResponse> result = chapterService.getAll();

        // then
        assertEquals(2, result.size());
        assertEquals(List.of(response1, response2), result);

        verify(chapterRepository).findAll();
        verify(chapterMapper).toResponse(chapter1);
        verify(chapterMapper).toResponse(chapter2);
    }


    @Test
    void getById_shouldReturnChapter_whenChapterExists() {
        // given
        Long id = 1L;

        Chapter chapter = new Chapter();
        ChapterResponse response = new ChapterResponse();

        when(chapterRepository.findById(id))
                .thenReturn(Optional.of(chapter));

        when(chapterMapper.toResponse(chapter))
                .thenReturn(response);

        // when
        ChapterResponse result = chapterService.getById(id);

        // then
        assertEquals(response, result);

        verify(chapterRepository).findById(id);
        verify(chapterMapper).toResponse(chapter);
    }


    @Test
    void getById_shouldThrowException_whenChapterDoesNotExist() {
        // given
        Long id = 1L;

        when(chapterRepository.findById(id))
                .thenReturn(Optional.empty());

        // when & then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> chapterService.getById(id)
        );

        assertEquals("Chapter not found with id: 1", exception.getMessage());

        verify(chapterRepository).findById(id);
        verifyNoInteractions(chapterMapper);
    }


    @Test
    void getByCourseId_shouldReturnChapters_whenCourseExists() {
        // given
        Long courseId = 10L;

        Chapter chapter1 = new Chapter();
        Chapter chapter2 = new Chapter();

        ChapterResponse response1 = new ChapterResponse();
        ChapterResponse response2 = new ChapterResponse();

        when(courseRepository.existsById(courseId))
                .thenReturn(true);

        when(chapterRepository.findByCourseIdOrderByOrderAsc(courseId))
                .thenReturn(List.of(chapter1, chapter2));

        when(chapterMapper.toResponse(chapter1))
                .thenReturn(response1);

        when(chapterMapper.toResponse(chapter2))
                .thenReturn(response2);

        // when
        List<ChapterResponse> result = chapterService.getByCourseId(courseId);

        // then
        assertEquals(2, result.size());
        assertEquals(List.of(response1, response2), result);

        verify(courseRepository).existsById(courseId);
        verify(chapterRepository).findByCourseIdOrderByOrderAsc(courseId);
    }


    @Test
    void getByCourseId_shouldThrowException_whenCourseDoesNotExist() {
        // given
        Long courseId = 10L;

        when(courseRepository.existsById(courseId))
                .thenReturn(false);

        // when & then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> chapterService.getByCourseId(courseId)
        );

        assertEquals("Course not found with id: 10", exception.getMessage());

        verify(courseRepository).existsById(courseId);
        verifyNoInteractions(chapterRepository);
        verifyNoInteractions(chapterMapper);
    }


    @Test
    void create_shouldCreateChapter_whenCourseExists() {
        // given
        Long courseId = 10L;

        ChapterCreateRequest request = new ChapterCreateRequest();
        request.setCourseId(courseId);
        request.setName("Java Basics");

        Course course = new Course();
        Chapter chapter = new Chapter();
        Chapter savedChapter = new Chapter();
        ChapterResponse response = new ChapterResponse();

        when(courseRepository.findById(courseId))
                .thenReturn(Optional.of(course));

        when(chapterMapper.toEntity(request))
                .thenReturn(chapter);

        when(chapterRepository.save(chapter))
                .thenReturn(savedChapter);

        when(chapterMapper.toResponse(savedChapter))
                .thenReturn(response);

        // when
        ChapterResponse result = chapterService.create(request);

        // then
        assertEquals(response, result);

        assertEquals(course, chapter.getCourse());

        verify(courseRepository).findById(courseId);
        verify(chapterMapper).toEntity(request);
        verify(chapterRepository).save(chapter);
        verify(chapterMapper).toResponse(savedChapter);
    }


    @Test
    void create_shouldThrowException_whenCourseDoesNotExist() {
        // given
        Long courseId = 10L;

        ChapterCreateRequest request = new ChapterCreateRequest();
        request.setCourseId(courseId);

        when(courseRepository.findById(courseId))
                .thenReturn(Optional.empty());

        // when & then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> chapterService.create(request)
        );

        assertEquals("Course not found with id: 10", exception.getMessage());

        verify(courseRepository).findById(courseId);
        verifyNoInteractions(chapterMapper);
        verifyNoInteractions(chapterRepository);
    }


    @Test
    void update_shouldUpdateChapter_whenChapterAndCourseExist() {
        // given
        Long chapterId = 1L;
        Long courseId = 10L;

        ChapterCreateRequest request = new ChapterCreateRequest();
        request.setCourseId(courseId);
        request.setName("Updated name");
        request.setDescription("Updated description");
        request.setOrder(2);

        Chapter chapter = new Chapter();
        Course course = new Course();
        ChapterResponse response = new ChapterResponse();

        when(chapterRepository.findById(chapterId))
                .thenReturn(Optional.of(chapter));

        when(courseRepository.findById(courseId))
                .thenReturn(Optional.of(course));

        when(chapterRepository.save(chapter))
                .thenReturn(chapter);

        when(chapterMapper.toResponse(chapter))
                .thenReturn(response);

        // when
        ChapterResponse result = chapterService.update(chapterId, request);

        // then
        assertEquals(response, result);

        assertEquals("Updated name", chapter.getName());
        assertEquals("Updated description", chapter.getDescription());
        assertEquals(2, chapter.getOrder());
        assertEquals(course, chapter.getCourse());

        verify(chapterRepository).findById(chapterId);
        verify(courseRepository).findById(courseId);
        verify(chapterRepository).save(chapter);
        verify(chapterMapper).toResponse(chapter);
    }


    @Test
    void update_shouldThrowException_whenChapterDoesNotExist() {
        // given
        Long chapterId = 1L;

        ChapterCreateRequest request = new ChapterCreateRequest();
        request.setCourseId(10L);

        when(chapterRepository.findById(chapterId))
                .thenReturn(Optional.empty());

        // when & then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> chapterService.update(chapterId, request)
        );

        assertEquals("Chapter not found with id: 1", exception.getMessage());

        verify(chapterRepository).findById(chapterId);
        verifyNoInteractions(courseRepository);
        verifyNoInteractions(chapterMapper);
    }


    @Test
    void update_shouldThrowException_whenCourseDoesNotExist() {
        // given
        Long chapterId = 1L;
        Long courseId = 10L;

        ChapterCreateRequest request = new ChapterCreateRequest();
        request.setCourseId(courseId);

        Chapter chapter = new Chapter();

        when(chapterRepository.findById(chapterId))
                .thenReturn(Optional.of(chapter));

        when(courseRepository.findById(courseId))
                .thenReturn(Optional.empty());

        // when & then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> chapterService.update(chapterId, request)
        );

        assertEquals("Course not found with id: 10", exception.getMessage());

        verify(chapterRepository).findById(chapterId);
        verify(courseRepository).findById(courseId);
        verify(chapterRepository, never()).save(any());
        verifyNoInteractions(chapterMapper);
    }


    @Test
    void delete_shouldDeleteChapter_whenChapterExists() {
        // given
        Long id = 1L;

        Chapter chapter = new Chapter();

        when(chapterRepository.findById(id))
                .thenReturn(Optional.of(chapter));

        // when
        chapterService.delete(id);

        // then
        verify(chapterRepository).findById(id);
        verify(chapterRepository).delete(chapter);
    }


    @Test
    void delete_shouldThrowException_whenChapterDoesNotExist() {
        // given
        Long id = 1L;

        when(chapterRepository.findById(id))
                .thenReturn(Optional.empty());

        // when & then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> chapterService.delete(id)
        );

        assertEquals("Chapter not found with id: 1", exception.getMessage());

        verify(chapterRepository).findById(id);
        verify(chapterRepository, never()).delete(any());
    }

}