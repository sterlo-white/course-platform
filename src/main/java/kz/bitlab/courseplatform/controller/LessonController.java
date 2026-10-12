package kz.bitlab.courseplatform.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import kz.bitlab.courseplatform.dto.LessonCreateRequest;
import kz.bitlab.courseplatform.dto.LessonResponse;
import kz.bitlab.courseplatform.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
public class LessonController {
    private final LessonService lessonService;

    @Operation(summary = "Get all lesson", description = "Return a list of all available lessons")
    @GetMapping
    public List<LessonResponse> getAll(){
        return lessonService.getAll();
    }

    @Operation(summary = "Get lesson by ID", description = " Returns a lesson by its unique identifier")
    @GetMapping("/{id}")
    public LessonResponse getById(@PathVariable Long id){
        return lessonService.getById(id);
    }

    @Operation(summary = "Get lessons by chapter ID", description = "Returns all lessons belonging to the specified chapter")
    @GetMapping("/chapter/{chapterId}")
    public  List<LessonResponse> getByChapterId(@PathVariable Long chapterId){
        return lessonService.getByChapterId(chapterId);
    }

    @Operation(summary = "Create lesson", description = "Creates a new lesson for a chapter")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LessonResponse create(@Valid @RequestBody LessonCreateRequest request){
        return lessonService.create(request);
    }

    @Operation(summary = "Update lesson", description = "Updates an existing lesson")
    @PutMapping("/{id}")
    public LessonResponse update(@PathVariable Long id, @Valid@RequestBody LessonCreateRequest request){
        return  lessonService.update(id, request);
    }

    @Operation(summary = " Delete lesson", description = "Deletes an existing lesson")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public  void delete(@PathVariable Long id){
        lessonService.delete(id);
    }
}
