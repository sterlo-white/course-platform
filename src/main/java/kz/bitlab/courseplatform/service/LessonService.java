package kz.bitlab.courseplatform.service;

import kz.bitlab.courseplatform.dto.LessonCreateRequest;
import kz.bitlab.courseplatform.dto.LessonResponse;
import kz.bitlab.courseplatform.entity.Chapter;
import kz.bitlab.courseplatform.entity.Lesson;
import kz.bitlab.courseplatform.exception.ResourceNotFoundException;
import kz.bitlab.courseplatform.mapper.LessonMapper;
import kz.bitlab.courseplatform.repository.ChapterRepository;
import kz.bitlab.courseplatform.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;


import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class LessonService {
    private static final Logger log = LoggerFactory.getLogger(LessonService.class);

    private final LessonRepository lessonRepository;
    private final ChapterRepository chapterRepository;
    private final LessonMapper lessonMapper;

    public List<LessonResponse> getAll(){
        return lessonRepository.findAll()
                .stream()
                .map(lessonMapper::toResponse)
                .toList();
    }

    public LessonResponse getById(Long id){
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Lesson not found with id: " + id));
        return lessonMapper.toResponse(lesson);
    }

    public List<LessonResponse> getByChapterId(Long chapterId){
        log.debug("Finding lessons for chapter id: {}", chapterId);

        if (!chapterRepository.existsById(chapterId)){
            throw new ResourceNotFoundException("Chapter not found with id: " + chapterId);
        }
        return lessonRepository.findByChapterIdOrderByOrderAsc(chapterId)
                .stream()
                .map(lessonMapper::toResponse)
                .toList();
    }

    public LessonResponse create(LessonCreateRequest request){
        log.info("Creating lesson: {}", request.getName());

        Chapter chapter = chapterRepository.findById(request.getChapterId())
                .orElseThrow(() -> {
                    log.error("Chapter not found with id: {}", request.getChapterId());

                    return new ResourceNotFoundException(
                            "Chapter not found with id: " + request.getChapterId());
                });
        Lesson lesson = lessonMapper.toEntity(request);
        lesson.setChapter(chapter);

        Lesson savedLesson = lessonRepository.save(lesson);

        log.info("Lesson created successfully with id: {}", savedLesson.getId());

        return lessonMapper.toResponse(savedLesson);
    }

    public LessonResponse update(Long id, LessonCreateRequest request){
        log.info("Updating lesson with id: {}", id);
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Lesson not found with id: {}", id);
                    return new ResourceNotFoundException(
                            "Lesson not found with id: " + id);
                });

        Chapter chapter = chapterRepository.findById(request.getChapterId())
                .orElseThrow(() -> {
                    log.error("Chapter not found with id: {}", request.getChapterId());

                    return new ResourceNotFoundException(
                            "Chapter not found with id: " + request.getChapterId());
                });

        lesson.setName(request.getName());
        lesson.setDescription(request.getDescription());
        lesson.setContent(request.getContent());
        lesson.setOrder(request.getOrder());
        lesson.setChapter(chapter);

        Lesson updatedLesson = lessonRepository.save(lesson);

        log.info("Lesson updated successfully with id: {}", id);

        return lessonMapper.toResponse(updatedLesson);
    }

    public void delete(Long id) {

        log.info("Deleting lesson with id: {}", id);

        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Lesson not found with id: {}", id);
                    return new ResourceNotFoundException(
                            "Lesson not found with id: " + id                    );
                });

        lessonRepository.delete(lesson);

        log.info("Lesson deleted successfully with id: {}", id);
    }

}
