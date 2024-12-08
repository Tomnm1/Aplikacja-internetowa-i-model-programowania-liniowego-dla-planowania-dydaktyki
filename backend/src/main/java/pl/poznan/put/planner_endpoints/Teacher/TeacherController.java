package pl.poznan.put.planner_endpoints.Teacher;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
/**
 * Controller for teacher resource
 */
@RestController
@RequestMapping("/teachers")
public class TeacherController {
    @Autowired
    private TeacherService teacherService;

    @Operation(summary = "Return all teachers")
    @GetMapping
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = TeacherDTO.class))
            )
    })
    @Transactional
    public List<TeacherDTO> getAllTeachers() {
        return teacherService.getAllTeachersDTO();
    }

    @Operation(summary = "Return teachers by id")
    @GetMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Teacher.class)
            )
    })
    @Transactional
    public TeacherDTO getTeacherByID(@PathVariable("id") Integer id) {
        return teacherService.getTeacherDTOByID(id);
    }

    @Operation(summary = "Check if user has admin privileges")
    @GetMapping("/testPrivileges/{id}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Boolean.class)
            )
    })
    @Transactional
    public Boolean getTeacherByEloginID(@PathVariable("id") String id) {
        return teacherService.checkIfAdminByEloginId(id);
    }

    @Operation(summary = "Create teachers from provided JSON")
    @PostMapping
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Teacher.class)
            )
    })
    public Teacher createTeacher(@RequestBody Teacher teacher){
        //teacher.degree = Degree.valueOf(teacher.degree.getDisplayName());
        return teacherService.createTeacher(teacher);
    }

    @Operation(summary = "Update specified teachers from provided JSON")
    @PutMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Teacher.class)
            )
    })
    public Teacher updateTeacherByID(@PathVariable("id") Integer id, @RequestBody Teacher teacherParams){
        return teacherService.updateTeacherByID(id, teacherParams);
    }

    @Operation(summary = "Update specified teacher's email from provided JSON")
    @PutMapping("email/{id}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Teacher.class)
            )
    })
    public Teacher updateTeacherEmailByID(@PathVariable("id") Integer id, @RequestBody String email){
        return teacherService.updateteacherEmailByID(id, email);
    }

    @Operation(summary = "Delete all teachers")
    @DeleteMapping
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(schema = @Schema(hidden = true))
    })
    public void deleteAllTeacher() {
        teacherService.deleteAllTeachers();
    }

    @Operation(summary = "Delete specified Teacher")
    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(schema = @Schema(hidden = true))
    })
    public void deleteTeacher(@PathVariable("id") Integer id) {
        teacherService.deleteTeacherByID(id);
    }
}
