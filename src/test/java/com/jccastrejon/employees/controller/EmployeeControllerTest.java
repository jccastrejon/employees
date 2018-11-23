package com.jccastrejon.employees.controller;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import com.jccastrejon.employees.controller.dto.create.CreateEmployeeDto;
import com.jccastrejon.employees.controller.dto.create.CreateEmployeesDto;
import com.jccastrejon.employees.controller.dto.update.UpdateEmployeeDto;
import com.jccastrejon.employees.controller.dto.update.UpdateEmployeesDto;
import com.jccastrejon.employees.model.Employee;
import com.jccastrejon.employees.repository.EmployeeRepository;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class EmployeeControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    EmployeeRepository employeeRepository;

    @After
    public void afterTest() {
        employeeRepository.deleteAll();
    }
    
	@Test
	public void contextLoads() {
    }

    @Test
    public void testGetInfo() {
        ResponseEntity<String> response;

        response = restTemplate.getForEntity("/", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testNullFindById() {
        ResponseEntity<String> response;

        response = restTemplate.getForEntity("/findById/123", String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testFindById() {
        ResponseEntity<String> response;

        response = restTemplate.getForEntity("/findById/" + createTestEmployee().getId(), String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testFindByIds() {
        ResponseEntity<String> response;

        response = restTemplate.getForEntity("/findByIds/1,2,4" + createTestEmployee().getId(), String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testInvalidCreateEmployees() {
        ResponseEntity<String> response;
        CreateEmployeeDto createEmployeeDto;
        CreateEmployeesDto createEmployeesDto;

        createEmployeeDto = new CreateEmployeeDto();
        createEmployeesDto = new CreateEmployeesDto();

        createEmployeeDto.setFirstName("firstName");
        createEmployeesDto.setEmployees(Arrays.asList(createEmployeeDto));

        response = restTemplate.postForEntity("/createEmployees", createEmployeesDto, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testCreateEmployees() {
        ResponseEntity<String> response;
        CreateEmployeeDto createEmployeeDto;
        CreateEmployeesDto createEmployeesDto;

        createEmployeeDto = new CreateEmployeeDto();
        createEmployeesDto = new CreateEmployeesDto();

        createEmployeeDto.setFirstName("firstName");
        createEmployeeDto.setLastName("lastName");
        createEmployeeDto.setDateOfBirth("2012-12-12");
        createEmployeeDto.setDateOfEmployment("2014-12-12");

        createEmployeesDto.setEmployees(Arrays.asList(createEmployeeDto));

        response = restTemplate.postForEntity("/createEmployees", createEmployeesDto, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testCreateEmployee() {
        ResponseEntity<String> response;
        CreateEmployeeDto createEmployeeDto;

        createEmployeeDto = new CreateEmployeeDto();

        createEmployeeDto.setFirstName("firstName");
        createEmployeeDto.setLastName("lastName");
        createEmployeeDto.setDateOfBirth("2012-12-12");
        createEmployeeDto.setDateOfEmployment("2014-12-12");

        response = restTemplate.postForEntity("/createEmployee", createEmployeeDto, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testInvalidUpdateEmployees() {
        ResponseEntity<String> response;
        UpdateEmployeeDto updateEmployeeDto;
        UpdateEmployeesDto updateEmployeesDto;

        updateEmployeeDto = new UpdateEmployeeDto();
        updateEmployeesDto = new UpdateEmployeesDto();

        updateEmployeeDto.setFirstName("firstName");
        updateEmployeeDto.setLastName("lastName");
        updateEmployeeDto.setDateOfBirth("2012-12-12");
        updateEmployeeDto.setDateOfEmployment("2014-12-12");

        updateEmployeesDto.setEmployees(Arrays.asList(updateEmployeeDto));

        response = restTemplate.exchange("/updateEmployees", HttpMethod.PUT, new HttpEntity<>(updateEmployeesDto), String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testUpdateEmployees() {
        ResponseEntity<String> response;
        UpdateEmployeeDto updateEmployeeDto;
        UpdateEmployeesDto updateEmployeesDto;

        updateEmployeeDto = new UpdateEmployeeDto();
        updateEmployeesDto = new UpdateEmployeesDto();

        updateEmployeeDto.setId(createTestEmployee().getId());
        updateEmployeeDto.setFirstName("firstName");
        updateEmployeeDto.setLastName("lastName");
        updateEmployeeDto.setDateOfBirth("2012-12-12");
        updateEmployeeDto.setDateOfEmployment("2014-12-12");

        updateEmployeesDto.setEmployees(Arrays.asList(updateEmployeeDto));

        response = restTemplate.exchange("/updateEmployees", HttpMethod.PUT, new HttpEntity<>(updateEmployeesDto), String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testUpdateEmployee() {
        ResponseEntity<String> response;
        UpdateEmployeeDto updateEmployeeDto;

        updateEmployeeDto = new UpdateEmployeeDto();

        updateEmployeeDto.setId(createTestEmployee().getId());
        updateEmployeeDto.setFirstName("firstName");
        updateEmployeeDto.setLastName("lastName");
        updateEmployeeDto.setDateOfBirth("2012-12-12");
        updateEmployeeDto.setDateOfEmployment("2014-12-12");


        response = restTemplate.exchange("/updateEmployee/" + updateEmployeeDto.getId(), HttpMethod.PUT, new HttpEntity<>(updateEmployeeDto), String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteEmployees() {
        ResponseEntity<String> response;

        response = restTemplate.exchange("/deleteEmployees/1,2,4", HttpMethod.DELETE, null, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteEmployee() {
        ResponseEntity<String> response;

        response = restTemplate.exchange("/deleteEmployee/error", HttpMethod.DELETE, null, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testInvalidGetEmployees() {
        ResponseEntity<String> response;

        response = restTemplate.getForEntity("/getEmployees?page={page}&size={size}", String.class, 10, 10);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testGetEmployees() {
        ResponseEntity<String> response;

        response = restTemplate.getForEntity("/getEmployees?page={page}&size={size}", String.class, 0, 10);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    // private methods

    private Employee createTestEmployee() {
        Employee returnValue;

        returnValue = new Employee();
        returnValue = employeeRepository.save(returnValue);

        return returnValue;
    }
}