package com.jccastrejon.employees.controller;

import java.lang.reflect.Type;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.google.common.reflect.TypeToken;
import com.jccastrejon.employees.controller.dto.CreateEmployeeDto;
import com.jccastrejon.employees.controller.dto.EmployeeDto;
import com.jccastrejon.employees.model.Employee;
import com.jccastrejon.employees.repository.employee.EmployeeRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<String> getInfo(HttpServletResponse response) {
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header(HttpHeaders.LOCATION, "/swagger-ui.html").build();
    }

    @GetMapping("/findById")
    public ResponseEntity<Employee> findById(@RequestParam(required = true) Long id) {
        return employeeRepository.findById(id)
            .map(x -> new ResponseEntity<>(x, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/createEmployees")
    public List<Employee> createEmployees(@Valid @RequestBody List<CreateEmployeeDto> employees) {
        List<Employee> returnValue = modelMapper.map(employees, EMPLOYEE_LIST_TYPE);
        return returnValue;
    }

    @PutMapping("/updateEmployees")
    public List<EmployeeDto> updateEmployees(@RequestBody List<Employee> employees) {
        return null;
    }

    @DeleteMapping("/deleteEmployees")
    public void deleteEmployees(@RequestBody List<EmployeeDto> employees) {

    }

    @GetMapping("/getEmployees")
    public List<Employee> getEmployees() {
        return null;
    }
}