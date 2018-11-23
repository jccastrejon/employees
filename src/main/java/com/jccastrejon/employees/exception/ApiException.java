package com.jccastrejon.employees.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    private String message;
}