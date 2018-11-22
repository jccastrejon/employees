package com.jccastrejon.employees.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="employee")
public class Employee {
    public enum Status {
        ACTIVE, INACTIVE
    }

    @Id
    @GeneratedValue
    private Long id;
    private String firstName;
    private String middleInitial;
    private String lastName;
    private LocalDate dateOfBirth;
    private LocalDate dateOfEmployment;

    @Enumerated(EnumType.STRING)
    private Status status;
}