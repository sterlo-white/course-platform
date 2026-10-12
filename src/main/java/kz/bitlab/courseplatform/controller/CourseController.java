package kz.bitlab.courseplatform.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kz.bitlab.courseplatform.dto.CourseCreateRequest;
import kz.bitlab.courseplatform.dto.CourseResponse;
import kz.bitlab.courseplatform.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Courses", description = "Course managent API")
@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor

public class CourseController {
    private final CourseService courseService;

    @Operation(summary = "Get all courses")
    @GetMapping
    public List<CourseResponse> getAll(){
        return courseService.getAll();
    }

    @Operation(summary = "Get course by ID", description = "Returns a course by its unique identifier")
    @GetMapping("/{id}")
    public CourseResponse getById(@PathVariable Long id){
        return courseService.getById(id);
    }

    @Operation(summary = "Create course", description = "Creates a new course")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CourseResponse create(@Valid @RequestBody CourseCreateRequest request){
        return courseService.create(request);
    }

    @Operation(summary = "Update course", description = "Updates an existing course")
    @PutMapping("/{id}")
    public CourseResponse update(
            @PathVariable Long id,
            @Valid @RequestBody CourseCreateRequest request
    ){
        return courseService.update(id, request);
    }

    @Operation(summary = "Delete course", description = "Deletes an existing course")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public  void delete(@PathVariable Long id){
        courseService.delete(id);
    }
}
