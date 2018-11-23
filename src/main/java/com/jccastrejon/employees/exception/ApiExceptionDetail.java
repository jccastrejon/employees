package com.jccastrejon.employees.exception;

import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiExceptionDetail {
    private HttpStatus status;
    private String message;
    private List<String> errors;
}