package pl.poznan.put.planner_endpoints.Employee;


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
 * Controller for employee resource
 */
@RestController
@RequestMapping("/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @Operation(summary = "Return all Employees")
    @GetMapping
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = Employee.class))
            )
    })
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @Operation(summary = "Return Employees by id")
    @GetMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Employee.class)
            )
    })
    public Optional<Employee> getEmployeeByID(@PathVariable("id") Integer id) {
        return employeeService.getEmployeeByID(id);
    }

    @Operation(summary = "Create Employees from provided JSON")
    @PostMapping
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Employee.class)
            )
    })
    // TODO add check to fail if entity already exists
    public Employee createEmployee(@RequestBody Employee employee){
        return employeeService.createEmployee(employee);
    }

    @Operation(summary = "Update specified Employees from provided JSON")
    @PutMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Employee.class)
            )
    })
    public Employee updateEmployeeByID(@PathVariable("id") Integer id, @RequestBody Employee employeeParams){
        return employeeService.updateEmployeeByID(id, employeeParams);
    }

    @Operation(summary = "Delete all Employees")
    @DeleteMapping
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(schema = @Schema(hidden = true))
    })
    public void deleteAllEmployee() {
        employeeService.deleteAllEmployees();
    }

    @Operation(summary = "Delete specified Employee")
    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(schema = @Schema(hidden = true))
    })
    public void deleteEmployee(@PathVariable("id") Integer id) {
        employeeService.deleteEmployeeByID(id);
    }
}
