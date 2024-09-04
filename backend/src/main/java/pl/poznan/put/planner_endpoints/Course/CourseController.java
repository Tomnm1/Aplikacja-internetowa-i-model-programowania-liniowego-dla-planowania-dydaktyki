package pl.poznan.put.planner_endpoints.Course;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller for Course resource
 */
@RestController // TODO use http response builders to create success and fail responses
@RequestMapping("/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @Operation(summary = "Return all Courses")
    @GetMapping
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = Course.class))
            )
    })
    public List<Course> getAllCourse() {
        return courseService.getAllCourse();
    }

    @Operation(summary = "Return Course by id")
    @GetMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Course.class)
            )
    })
    public Optional<Course> getCourseByID(@PathVariable("id") Integer id) {
        return courseService.getCourseByID(id);
    }

    @Operation(summary = "Create Course from provided JSON")
    @PostMapping
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Course.class)
            )
    })
    // TODO add check to fail if entity already exists
    public Course createCourse(@RequestBody Course course){
        return courseService.createCourse(course);
    }

    @Operation(summary = "Update specified Course from provided JSON")
    @PutMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Course.class)
            )
    })
    public Course updateCourseByID(@PathVariable("id") Integer id, @RequestBody Course courseParams){
        return courseService.updateCourseByID(id, courseParams);
    }

    @Operation(summary = "Delete all Courses")
    @DeleteMapping
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(schema = @Schema(hidden = true))
    })
    public void deleteAllCourses() {
        courseService.deleteAllCourses();
    }

    @Operation(summary = "Delete specified Course")
    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(schema = @Schema(hidden = true))
    })
    public void deleteCourse(@PathVariable("id") Integer id) {
        courseService.deleteCourseByID(id);
    }
}
