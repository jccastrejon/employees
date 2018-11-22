package com.jccastrejon.employees.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDto {
    private Long id;
    private String firstName;
    private String middleInitial;
    private String lastName;
    private String dateOfBirth;
    private String dateOfEmployment;
}