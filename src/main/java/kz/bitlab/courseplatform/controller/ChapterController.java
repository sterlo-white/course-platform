package kz.bitlab.courseplatform.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import kz.bitlab.courseplatform.dto.ChapterCreateRequest;
import kz.bitlab.courseplatform.dto.ChapterResponse;
import kz.bitlab.courseplatform.service.ChapterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chapters")
@RequiredArgsConstructor
public class ChapterController {
    private final ChapterService chapterService;

    @Operation(summary = "Get all chapters", description = "Returns a list of all available chapters")
    @GetMapping
    public List<ChapterResponse> getAll(){
        return chapterService.getAll();
    }

    @Operation(summary = "Get chapter by ID", description = "Return a chapter by its unique identifier")
    @GetMapping("/{id}")
    public ChapterResponse getById(@PathVariable Long id){
        return chapterService.getById(id);
    }

    @Operation(summary = "Get chapters by course Id", description = "Return all chapters belonging to the specified course")
    @GetMapping("/course/{courseId}")
    public List<ChapterResponse> getByCourseId(@PathVariable Long courseId){
        return chapterService.getByCourseId(courseId);
    }

    @Operation(summary = "Create chapter", description = "Creates a new chapter for course")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ChapterResponse create(@Valid @RequestBody ChapterCreateRequest request){
        return chapterService.create(request);
    }

    @Operation(summary = "Update chapter", description = "Updates an existing chapter")
    @PutMapping("/{id}")
    public ChapterResponse update(@PathVariable Long id, @Valid @RequestBody ChapterCreateRequest request){
        return chapterService.update(id, request);
    }

    @Operation(summary = "Delete chapter", description = "Deletes an existing chapter")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public  void delete(@PathVariable Long id)
    {
        chapterService.delete(id);
    }}
