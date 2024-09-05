package pl.poznan.put.planner_endpoints.Employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Business logic for employees
 */
@Service
public class EmployeeService {
    /**
     * Employees repository
     */
    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * Returns all Employees
     * @return list of Employees
     */
    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    /**
     * For pagination - returns Employees from given page
     * @param page number of page to return
     * @param size total number of pages
     * @return Page object containing all found Employees objects
     */
    public Page<Employee> getEmployeePage(Integer page, Integer size){
        return employeeRepository.findAll(PageRequest.of(page,size));
    }

    /**
     * Finds Subject by id or empty Optional
     * @param id subject id
     * @return Optional - empty or with Subject
     */
    public Optional<Employee> getEmployeeByID(Integer id){
        return employeeRepository.findById(id);
    }

    /**
     * Creates an Employee
     * @param employee object to be inserted into DB
     * @return saved Employee
     */
    public Employee createEmployee(Employee employee){
        return employeeRepository.save(employee);
    }

    /**
     * Updates existing Employee if it exists
     * @param id id
     * @param employeeParams new values in JSON format
     * @return saved Employee or null
     */
    public Employee updateEmployeeByID(Integer id, Employee employeeParams){
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isPresent()) {
            Employee oldEmployee = employee.get();
            oldEmployee.name = employeeParams.name;
            oldEmployee.surname = employeeParams.surname;
            oldEmployee.degree = employeeParams.degree;
            oldEmployee.preferences = employeeParams.preferences;
            return employeeRepository.save(oldEmployee);
        } else {
            return null;
        }
    }

    /**
     * Deletes Employee by ID
     * @param id id
     */
    public void deleteEmployeeByID(Integer id){
        employeeRepository.deleteById(id);
    }

    /**
     * Deletes all Employees
     */
    public void deleteAllEmployees(){
        employeeRepository.deleteAll();
    }
    // Tu będzie więcej...
}
