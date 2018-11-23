package com.jccastrejon.employees.controller.dto.update;

import javax.validation.constraints.NotNull;

import com.jccastrejon.employees.controller.dto.create.CreateEmployeeDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEmployeeDto extends CreateEmployeeDto {
    
    @NotNull
    private Long id;
}