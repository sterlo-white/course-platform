package kz.bitlab.courseplatform;

import kz.bitlab.courseplatform.dto.LessonCreateRequest;
import kz.bitlab.courseplatform.dto.LessonResponse;
import kz.bitlab.courseplatform.entity.Chapter;
import kz.bitlab.courseplatform.entity.Lesson;
import kz.bitlab.courseplatform.exception.ResourceNotFoundException;
import kz.bitlab.courseplatform.mapper.LessonMapper;
import kz.bitlab.courseplatform.repository.ChapterRepository;
import kz.bitlab.courseplatform.repository.LessonRepository;
import kz.bitlab.courseplatform.service.LessonService;
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
class LessonServiceTest {

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private ChapterRepository chapterRepository;

    @Mock
    private LessonMapper lessonMapper;

    @InjectMocks
    private LessonService lessonService;


    @Test
    void getAll_shouldReturnAllLessons() {
        // given
        Lesson lesson1 = new Lesson();
        Lesson lesson2 = new Lesson();

        LessonResponse response1 = new LessonResponse();
        LessonResponse response2 = new LessonResponse();

        when(lessonRepository.findAll())
                .thenReturn(List.of(lesson1, lesson2));

        when(lessonMapper.toResponse(lesson1))
                .thenReturn(response1);

        when(lessonMapper.toResponse(lesson2))
                .thenReturn(response2);

        // when
        List<LessonResponse> result = lessonService.getAll();

        // then
        assertEquals(2, result.size());
        assertEquals(List.of(response1, response2), result);

        verify(lessonRepository).findAll();
        verify(lessonMapper).toResponse(lesson1);
        verify(lessonMapper).toResponse(lesson2);
    }


    @Test
    void getById_shouldReturnLesson_whenLessonExists() {
        // given
        Long id = 1L;

        Lesson lesson = new Lesson();
        LessonResponse response = new LessonResponse();

        when(lessonRepository.findById(id))
                .thenReturn(Optional.of(lesson));

        when(lessonMapper.toResponse(lesson))
                .thenReturn(response);

        // when
        LessonResponse result = lessonService.getById(id);

        // then
        assertEquals(response, result);

        verify(lessonRepository).findById(id);
        verify(lessonMapper).toResponse(lesson);
    }


    @Test
    void getById_shouldThrowException_whenLessonDoesNotExist() {
        // given
        Long id = 1L;

        when(lessonRepository.findById(id))
                .thenReturn(Optional.empty());

        // when & then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> lessonService.getById(id)
        );

        assertEquals(
                "Lesson not found with id: 1",
                exception.getMessage()
        );

        verify(lessonRepository).findById(id);
        verifyNoInteractions(lessonMapper);
    }


    @Test
    void getByChapterId_shouldReturnLessons_whenChapterExists() {
        // given
        Long chapterId = 10L;

        Lesson lesson1 = new Lesson();
        Lesson lesson2 = new Lesson();

        LessonResponse response1 = new LessonResponse();
        LessonResponse response2 = new LessonResponse();

        when(chapterRepository.existsById(chapterId))
                .thenReturn(true);

        when(lessonRepository.findByChapterIdOrderByOrderAsc(chapterId))
                .thenReturn(List.of(lesson1, lesson2));

        when(lessonMapper.toResponse(lesson1))
                .thenReturn(response1);

        when(lessonMapper.toResponse(lesson2))
                .thenReturn(response2);

        // when
        List<LessonResponse> result =
                lessonService.getByChapterId(chapterId);

        // then
        assertEquals(2, result.size());
        assertEquals(
                List.of(response1, response2),
                result
        );

        verify(chapterRepository).existsById(chapterId);
        verify(lessonRepository)
                .findByChapterIdOrderByOrderAsc(chapterId);
    }


    @Test
    void getByChapterId_shouldThrowException_whenChapterDoesNotExist() {
        // given
        Long chapterId = 10L;

        when(chapterRepository.existsById(chapterId))
                .thenReturn(false);

        // when & then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> lessonService.getByChapterId(chapterId)
        );

        assertEquals(
                "Chapter not found with id: 10",
                exception.getMessage()
        );

        verify(chapterRepository).existsById(chapterId);
        verifyNoInteractions(lessonRepository);
        verifyNoInteractions(lessonMapper);
    }


    @Test
    void create_shouldCreateLesson_whenChapterExists() {
        // given
        Long chapterId = 10L;

        LessonCreateRequest request = new LessonCreateRequest();
        request.setChapterId(chapterId);
        request.setName("Java Variables");
        request.setDescription("Variables and data types");
        request.setContent("Java variables store data values.");
        request.setOrder(1);

        Chapter chapter = new Chapter();
        Lesson lesson = new Lesson();
        Lesson savedLesson = new Lesson();
        LessonResponse response = new LessonResponse();

        when(chapterRepository.findById(chapterId))
                .thenReturn(Optional.of(chapter));

        when(lessonMapper.toEntity(request))
                .thenReturn(lesson);

        when(lessonRepository.save(lesson))
                .thenReturn(savedLesson);

        when(lessonMapper.toResponse(savedLesson))
                .thenReturn(response);

        // when
        LessonResponse result = lessonService.create(request);

        // then
        assertEquals(response, result);
        assertEquals(chapter, lesson.getChapter());

        verify(chapterRepository).findById(chapterId);
        verify(lessonMapper).toEntity(request);
        verify(lessonRepository).save(lesson);
        verify(lessonMapper).toResponse(savedLesson);
    }


    @Test
    void create_shouldThrowException_whenChapterDoesNotExist() {
        // given
        Long chapterId = 10L;

        LessonCreateRequest request = new LessonCreateRequest();
        request.setChapterId(chapterId);

        when(chapterRepository.findById(chapterId))
                .thenReturn(Optional.empty());

        // when & then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> lessonService.create(request)
        );

        assertEquals(
                "Chapter not found with id: 10",
                exception.getMessage()
        );

        verify(chapterRepository).findById(chapterId);
        verifyNoInteractions(lessonMapper);
        verifyNoInteractions(lessonRepository);
    }


    @Test
    void update_shouldUpdateLesson_whenLessonAndChapterExist() {
        // given
        Long lessonId = 1L;
        Long chapterId = 10L;

        LessonCreateRequest request = new LessonCreateRequest();
        request.setChapterId(chapterId);
        request.setName("Updated Java Lesson");
        request.setDescription("Updated description");
        request.setContent("Updated content");
        request.setOrder(2);

        Lesson lesson = new Lesson();
        Chapter chapter = new Chapter();
        LessonResponse response = new LessonResponse();

        when(lessonRepository.findById(lessonId))
                .thenReturn(Optional.of(lesson));

        when(chapterRepository.findById(chapterId))
                .thenReturn(Optional.of(chapter));

        when(lessonRepository.save(lesson))
                .thenReturn(lesson);

        when(lessonMapper.toResponse(lesson))
                .thenReturn(response);

        // when
        LessonResponse result =
                lessonService.update(lessonId, request);

        // then
        assertEquals(response, result);

        assertEquals(
                "Updated Java Lesson",
                lesson.getName()
        );

        assertEquals(
                "Updated description",
                lesson.getDescription()
        );

        assertEquals(
                "Updated content",
                lesson.getContent()
        );

        assertEquals(2, lesson.getOrder());
        assertEquals(chapter, lesson.getChapter());

        verify(lessonRepository).findById(lessonId);
        verify(chapterRepository).findById(chapterId);
        verify(lessonRepository).save(lesson);
        verify(lessonMapper).toResponse(lesson);
    }


    @Test
    void update_shouldThrowException_whenLessonDoesNotExist() {
        // given
        Long lessonId = 1L;
        Long chapterId = 10L;

        LessonCreateRequest request = new LessonCreateRequest();
        request.setChapterId(chapterId);

        when(lessonRepository.findById(lessonId))
                .thenReturn(Optional.empty());

        // when & then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> lessonService.update(lessonId, request)
        );

        assertEquals(
                "Lesson not found with id: 1",
                exception.getMessage()
        );

        verify(lessonRepository).findById(lessonId);
        verifyNoInteractions(chapterRepository);
        verifyNoInteractions(lessonMapper);
    }


    @Test
    void update_shouldThrowException_whenChapterDoesNotExist() {
        // given
        Long lessonId = 1L;
        Long chapterId = 10L;

        LessonCreateRequest request = new LessonCreateRequest();
        request.setChapterId(chapterId);

        Lesson lesson = new Lesson();

        when(lessonRepository.findById(lessonId))
                .thenReturn(Optional.of(lesson));

        when(chapterRepository.findById(chapterId))
                .thenReturn(Optional.empty());

        // when & then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> lessonService.update(lessonId, request)
        );

        assertEquals(
                "Chapter not found with id: 10",
                exception.getMessage()
        );

        verify(lessonRepository).findById(lessonId);
        verify(chapterRepository).findById(chapterId);
        verify(lessonRepository, never()).save(any());
        verifyNoInteractions(lessonMapper);
    }


    @Test
    void delete_shouldDeleteLesson_whenLessonExists() {
        // given
        Long id = 1L;

        Lesson lesson = new Lesson();

        when(lessonRepository.findById(id))
                .thenReturn(Optional.of(lesson));

        // when
        lessonService.delete(id);

        // then
        verify(lessonRepository).findById(id);
        verify(lessonRepository).delete(lesson);
    }


    @Test
    void delete_shouldThrowException_whenLessonDoesNotExist() {
        // given
        Long id = 1L;

        when(lessonRepository.findById(id))
                .thenReturn(Optional.empty());

        // when & then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> lessonService.delete(id)
        );

        assertEquals(
                "Lesson not found with id: 1",
                exception.getMessage()
        );

        verify(lessonRepository).findById(id);
        verify(lessonRepository, never()).delete(any());
    }


}