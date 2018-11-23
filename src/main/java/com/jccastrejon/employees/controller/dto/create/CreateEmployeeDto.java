package com.jccastrejon.employees.controller.dto.create;

import java.time.LocalDate;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateEmployeeDto {
    @JsonIgnore
    private boolean birthBeforeEmployment;

    @NotNull
    private String firstName;

    private String middleInitial;

    @NotNull
    private String lastName;

    @NotNull
    private String dateOfBirth;

    @NotNull
    private String dateOfEmployment;

    @AssertTrue()
    public boolean getBirthBeforeEmployment() {
        boolean returnValue;

        try {
            returnValue = LocalDate.parse(dateOfBirth).isBefore(LocalDate.parse(dateOfEmployment));
        } catch (Exception e) {
            returnValue = false;
        }

        return returnValue;
    }
}