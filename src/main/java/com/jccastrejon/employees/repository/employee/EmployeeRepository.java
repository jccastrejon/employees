package com.jccastrejon.employees.repository.employee;

import com.jccastrejon.employees.model.Employee;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{

}