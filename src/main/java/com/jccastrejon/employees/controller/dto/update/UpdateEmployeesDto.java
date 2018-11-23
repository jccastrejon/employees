package com.jccastrejon.employees.controller.dto.update;

import java.util.List;

import javax.validation.Valid;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEmployeesDto {
    @Valid
    private List<UpdateEmployeeDto> employees;
}