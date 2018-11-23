package com.jccastrejon.employees.controller;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.google.common.reflect.TypeToken;
import com.jccastrejon.employees.controller.dto.create.CreateEmployeeDto;
import com.jccastrejon.employees.controller.dto.create.CreateEmployeesDto;
import com.jccastrejon.employees.controller.dto.update.UpdateEmployeeDto;
import com.jccastrejon.employees.controller.dto.update.UpdateEmployeesDto;
import com.jccastrejon.employees.exception.ApiException;
import com.jccastrejon.employees.model.Employee;
import com.jccastrejon.employees.model.Employee.Status;
import com.jccastrejon.employees.repository.EmployeeRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * EmployeeController - Defines employee REST endpoints to :
 * <ul>
 * <li>Get employees by an ID</li> 
 * <li>Create new employees</li>
 * <li>Update existing employees</li>
 * <li>Delete employees</li>
 * <li>Get all employees</li>
 * </ul>
 * 
 * This controller relies on data transfer objects that are used as coarse-grained data containers and to better validate the data models used by all endpoints.
 * <p>
 * All of the database operations are executed through the JPA {@link EmployeeRepository} that serves to abstract the data layer.
 * <p>
 * We rely on model mappers ({@link ModelMapper}) to transform between our DTOs and JPA model objects. This helps us separate both service layers and avoid development dependencies.
 * <p>
 * We rely on Spring's AOP support for exception ({@link CustomExceptionHandler}) and security handling. This approach allows us to add these cross-cutting concerns without modifying our core endpoints logic.
 * 
 * 
 * @author jccastrejon
 */
@RestController
public class EmployeeController {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    EmployeeRepository employeeRepository;

    private final static Type EMPLOYEE_LIST_TYPE = new TypeToken<List<Employee>>() {
        private static final long serialVersionUID = 1L;
    }.getType();

    @GetMapping("/")
    public ResponseEntity<String> swagger(HttpServletResponse response) {
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header(HttpHeaders.LOCATION, "/swagger-ui.html")
                .build();
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Employee> findById(@PathVariable Long id) {
        return employeeRepository.findByIdAndStatus(id, Status.ACTIVE).map(x -> new ResponseEntity<>(x, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/findByIds/{ids}")
    public List<Employee> findByIds(@PathVariable String ids) {
        return employeeRepository.findAllByIdInAndStatus(
            Stream.of(ids.trim().split(","))
            .map(x -> parseId(x, ids))
            .collect(Collectors.toList()), Status.ACTIVE);
    }

    @PostMapping("/createEmployee")
    public Employee createEmployee(@RequestBody @Valid CreateEmployeeDto employee) {
        return employeeRepository.save(modelMapper.map(employee, Employee.class));
    }

    @PostMapping("/createEmployees")
    public List<Employee> createEmployees(@RequestBody @Valid CreateEmployeesDto employees) {
        return employeeRepository.saveAll(modelMapper.map(employees.getEmployees(), EMPLOYEE_LIST_TYPE));
    }

    @PutMapping("/updateEmployee/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody @Valid UpdateEmployeeDto employee) {
        Employee updateEmployee;

        updateEmployee = modelMapper.map(employee, Employee.class);
        return employeeRepository.findById(id).map(x -> new ResponseEntity<>(employeeRepository.save(updateEmployee), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/updateEmployees")
    public List<Employee> updateEmployees(@RequestBody @Valid UpdateEmployeesDto employees) {
        List<Long> updateIds;
        List<Employee> updateEmployees;

        updateIds = employees.getEmployees().stream()
            .map(UpdateEmployeeDto::getId)
            .collect(Collectors.toList());


        updateEmployees = employeeRepository.findAllById(updateIds);
        if (updateIds.size() != updateEmployees.size()) {
            throw new ApiException("Please confirm the following IDs are valid and unique: " + updateIds);
        }

        return employeeRepository.saveAll(modelMapper.map(employees.getEmployees(), EMPLOYEE_LIST_TYPE));
    }

    @DeleteMapping("/secure/deleteEmployee/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        employeeRepository.findById(id).ifPresent(x -> {
            x.setStatus(Status.INACTIVE);
            employeeRepository.save(x);
        });
    }

    @DeleteMapping("/secure/deleteEmployees/{ids}")
    public void deleteEmployees(@PathVariable String ids) {
        List<Employee> deleteEmployees;

        deleteEmployees = employeeRepository.findAllById(
            Stream.of(ids.trim().split(","))
            .map(x -> parseId(x, ids))
            .collect(Collectors.toList()));

        deleteEmployees.stream().forEach(x -> x.setStatus(Status.INACTIVE));
        employeeRepository.saveAll(deleteEmployees);
    }

    @GetMapping(value = "/getEmployees")
    public Page<Employee> getEmployees(@RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="10") int size) {
        Page<Employee> employeesPage;

        employeesPage = employeeRepository.findAllByStatus(Status.ACTIVE, PageRequest.of(page, size));
        if (page > employeesPage.getTotalPages()) {
            throw new ApiException(
                    "Invalid page. Requested: " + page + " but maximum value is: " + employeesPage.getTotalPages());
        }

        return employeesPage;
    }

    // private methods

    private long parseId(String id, String ids) {
        try {
            return Long.parseLong(id);
        } catch(NumberFormatException e) {
            throw new ApiException("Please verify that the ids are numeric and that they are separated by commas: " + ids);
        }
    }
}