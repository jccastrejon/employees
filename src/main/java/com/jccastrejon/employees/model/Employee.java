package com.jccastrejon.employees.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name="first_name")
    private String firstName;

    @Column(name="middle_initial")
    private String middleInitial;

    @Column(name="last_name")
    private String lastName;

    @Column(name="date_birth")
    private LocalDate dateOfBirth;

    @Column(name="date_employment")
    private LocalDate dateOfEmployment;

    @Enumerated(EnumType.STRING)
    @Column(name="status")
    private Status status = Status.ACTIVE;
}