package com.jccastrejon.employees.controller.dto.create;

import java.util.List;

import javax.validation.Valid;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateEmployeesDto {
    @Valid
    private List<CreateEmployeeDto> employees;
}