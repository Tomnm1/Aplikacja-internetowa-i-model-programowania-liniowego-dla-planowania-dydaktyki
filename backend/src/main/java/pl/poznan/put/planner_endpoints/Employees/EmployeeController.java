package pl.poznan.put.planner_endpoints.Employees;


import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @Operation(summary = "Return Employees by id")
    @GetMapping("/{id}")
    public Optional<Employee> getEmployeeByID(@PathVariable("id") Integer id) {
        return employeeService.getEmployeeByID(id);
    }

    @Operation(summary = "Create Employees from provided JSON")
    @PostMapping
    // TODO add check to fail if entity already exists
    public Employee createEmployee(@RequestBody Employee employee){
        return employeeService.createEmployee(employee);
    }

    @Operation(summary = "Update specified Employees from provided JSON")
    @PutMapping("/{id}")
    public Employee updateEmployeeByID(@PathVariable("id") Integer id, @RequestBody Employee employeeParams){
        return employeeService.updateEmployeeByID(id, employeeParams);
    }

    @Operation(summary = "Delete all Employees")
    @DeleteMapping
    public void deleteAllEmployee() {
        employeeService.deleteAllEmployees();
    }

    @Operation(summary = "Delete specified Employee")
    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable("id") Integer id) {
        employeeService.deleteEmployeeByID(id);
    }
}
