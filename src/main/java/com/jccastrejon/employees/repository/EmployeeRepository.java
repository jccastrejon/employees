package com.jccastrejon.employees.repository;

import java.util.List;
import java.util.Optional;

import com.jccastrejon.employees.model.Employee;
import com.jccastrejon.employees.model.Employee.Status;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{
    Optional<Employee> findByIdAndStatus(long id, Status status);
    List<Employee> findAllByIdInAndStatus(List<Long> ids, Status status);
    Page<Employee> findAllByStatus(Status status, Pageable pageable);
}