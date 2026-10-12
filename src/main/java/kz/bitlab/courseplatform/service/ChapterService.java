package kz.bitlab.courseplatform.service;

import kz.bitlab.courseplatform.dto.ChapterCreateRequest;
import kz.bitlab.courseplatform.dto.ChapterResponse;
import kz.bitlab.courseplatform.entity.Chapter;
import kz.bitlab.courseplatform.entity.Course;
import kz.bitlab.courseplatform.exception.ResourceNotFoundException;
import kz.bitlab.courseplatform.mapper.ChapterMapper;
import kz.bitlab.courseplatform.repository.ChapterRepository;
import kz.bitlab.courseplatform.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChapterService {
    private final ChapterRepository chapterRepository;
    private final CourseRepository courseRepository;
    private final ChapterMapper chapterMapper;

    public List<ChapterResponse> getAll(){
        log.info("Fetching all chapters");
        List<ChapterResponse> chapters =  chapterRepository.findAll()
                .stream()
                .map(chapterMapper::toResponse)
                .toList();
        log.info("Successfully fetched {} chapters", chapters.size());
        return chapters;
    }

    public ChapterResponse getById(Long id){
        Chapter chapter = chapterRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Chapter not found with id: " +id));
        return chapterMapper.toResponse(chapter);
    }

    public List<ChapterResponse> getByCourseId(Long courseId){
        if (!courseRepository.existsById(courseId)){
            throw  new ResourceNotFoundException("Course not found with id: " + courseId);
        }
        return chapterRepository.findByCourseIdOrderByOrderAsc(courseId)
                .stream()
                .map(chapterMapper:: toResponse)
                .toList();
    }

    public ChapterResponse create(ChapterCreateRequest request){
        log.info("Creating chapter with name: {}", request.getName());
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(()-> new ResourceNotFoundException("Course not found with id: " + request.getCourseId()));
        Chapter chapter = chapterMapper.toEntity(request);
        chapter.setCourse(course);
        Chapter savedChapter = chapterRepository.save(chapter);
        log.info("Successfully created chapter with id: {}", savedChapter.getId());
        return chapterMapper.toResponse(savedChapter);
    }

    public ChapterResponse update(Long id, ChapterCreateRequest request){
        log.info("Updating chapter with id: {}", id);
        Chapter chapter = chapterRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Chapter not found with id: " + id));
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(()-> new ResourceNotFoundException("Course not found with id: " + request.getCourseId()));
        chapter.setName(request.getName());
        chapter.setDescription(request.getDescription());
        chapter.setOrder(request.getOrder());
        chapter.setCourse(course);

        Chapter upDatedChapter = chapterRepository.save(chapter);
        log.info("Successfully updated chapter with id: {}", upDatedChapter.getId());
        return  chapterMapper.toResponse(upDatedChapter);
    }

    public void delete (Long id){
        log.info("Deleting chapter with id: {}", id);
        Chapter chapter = chapterRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Chapter not found with id: " + id));
        chapterRepository.delete(chapter);
        log.info("Successfully deleted chapter with id: {}", id);
    }
}
