package com.jccastrejon.employees.controller.dto;

import java.time.LocalDate;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateEmployeeDto {
    @NotNull
    private String firstName;

    @NotNull
    private String middleInitial;

    @NotNull
    private String lastName;

    @NotNull
    @Past
    private String dateOfBirth;

    @NotNull
    @Past
    private String dateOfEmployment;

    @AssertTrue(message="Date of employment should be after date of birth")
    private boolean validateDates() {
        boolean returnValue;

        try {
            returnValue = LocalDate.parse(dateOfBirth).isBefore(LocalDate.parse(dateOfEmployment));
        } catch (Exception e) {
            returnValue = false;
        }

        return returnValue;
    }
}