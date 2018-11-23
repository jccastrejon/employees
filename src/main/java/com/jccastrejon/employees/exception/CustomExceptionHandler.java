package com.jccastrejon.employees.exception;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

//Based on https://www.baeldung.com/global-error-handler-in-a-spring-rest-api
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    // 400

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException exception,
            final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
                ApiExceptionDetail apiExceptionDetail;
        List<String> errors;

        errors = new ArrayList<String>();
        exception.getBindingResult().getFieldErrors().stream()
                .forEach(x -> errors.add(x.getField() + ": " + x.getDefaultMessage()));
        exception.getBindingResult().getGlobalErrors().stream()
                .forEach(x -> errors.add(x.getObjectName() + ": " + x.getDefaultMessage()));

        apiExceptionDetail = new ApiExceptionDetail(HttpStatus.BAD_REQUEST, exception.getLocalizedMessage(), errors);
        return handleExceptionInternal(exception, apiExceptionDetail, headers, apiExceptionDetail.getStatus(), request);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(final BindException exception, final HttpHeaders headers,
            final HttpStatus status, final WebRequest request) {
                ApiExceptionDetail apiExceptionDetail;
        List<String> errors;
        errors = new ArrayList<String>();
        exception.getBindingResult().getFieldErrors().stream()
                .forEach(x -> errors.add(x.getField() + ": " + x.getDefaultMessage()));
        exception.getBindingResult().getGlobalErrors().stream()
                .forEach(x -> errors.add(x.getObjectName() + ": " + x.getDefaultMessage()));

        apiExceptionDetail = new ApiExceptionDetail(HttpStatus.BAD_REQUEST, exception.getLocalizedMessage(), errors);
        return handleExceptionInternal(exception, apiExceptionDetail, headers, apiExceptionDetail.getStatus(), request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(final TypeMismatchException exception,
            final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        String error;
        ApiExceptionDetail apiExceptionDetail;

        error = exception.getValue() + " value for " + exception.getPropertyName() + " should be of type "
                + exception.getRequiredType();

        apiExceptionDetail = new ApiExceptionDetail(HttpStatus.BAD_REQUEST, exception.getLocalizedMessage(), Arrays.asList(error));
        return new ResponseEntity<Object>(apiExceptionDetail, new HttpHeaders(), apiExceptionDetail.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            final MissingServletRequestParameterException exception, final HttpHeaders headers, final HttpStatus status,
            final WebRequest request) {
        String error;
        ApiExceptionDetail apiExceptionDetail;

        error = exception.getParameterName() + " parameter is missing";
        apiExceptionDetail = new ApiExceptionDetail(HttpStatus.BAD_REQUEST, exception.getLocalizedMessage(), Arrays.asList(error));
        return new ResponseEntity<Object>(apiExceptionDetail, new HttpHeaders(), apiExceptionDetail.getStatus());
    }

    @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(final MethodArgumentTypeMismatchException ex,
            final WebRequest request) {
        String error;
        ApiExceptionDetail apiExceptionDetail;
        
        error = ex.getName() + " should be of type " + ex.getRequiredType().getName();
        apiExceptionDetail = new ApiExceptionDetail(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), Arrays.asList(error));

        return new ResponseEntity<Object>(apiExceptionDetail, new HttpHeaders(), apiExceptionDetail.getStatus());
    }

    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity<Object> handleConstraintViolation(final ConstraintViolationException exception,
            final WebRequest request) {
                ApiExceptionDetail apiExceptionDetail;
        List<String> errors;

        errors = new ArrayList<String>();
        exception.getConstraintViolations().stream().forEach(
                x -> errors.add(x.getRootBeanClass().getName() + " " + x.getPropertyPath() + ": " + x.getMessage()));

        apiExceptionDetail = new ApiExceptionDetail(HttpStatus.BAD_REQUEST, exception.getLocalizedMessage(), errors);
        return new ResponseEntity<Object>(apiExceptionDetail, new HttpHeaders(), apiExceptionDetail.getStatus());
    }

    // 404

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(final NoHandlerFoundException ex,
            final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        String error;
        ApiExceptionDetail apiExceptionDetail;

        error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();
        apiExceptionDetail = new ApiExceptionDetail(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), Arrays.asList(error));
        return new ResponseEntity<Object>(apiExceptionDetail, new HttpHeaders(), apiExceptionDetail.getStatus());
    }

    // 405

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            final HttpRequestMethodNotSupportedException exception, final HttpHeaders headers, final HttpStatus status,
            final WebRequest request) {
        final StringBuilder errorBuilder;
        ApiExceptionDetail apiExceptionDetail;

        errorBuilder = new StringBuilder();
        errorBuilder.append(exception.getMethod());
        errorBuilder.append(" method is not supported for this request. Supported methods are ");
        exception.getSupportedHttpMethods().forEach(t -> errorBuilder.append(t + " "));

        apiExceptionDetail = new ApiExceptionDetail(HttpStatus.METHOD_NOT_ALLOWED, exception.getLocalizedMessage(),
                Arrays.asList(errorBuilder.toString()));
        return new ResponseEntity<Object>(apiExceptionDetail, new HttpHeaders(), apiExceptionDetail.getStatus());
    }

    // 415

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(final HttpMediaTypeNotSupportedException exception,
            final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        final StringBuilder errorBuilder;
        ApiExceptionDetail apiExceptionDetail;

        errorBuilder = new StringBuilder();
        errorBuilder.append(exception.getContentType());
        errorBuilder.append(" media type is not supported. Supported media types are ");
        exception.getSupportedMediaTypes().forEach(t -> errorBuilder.append(t + " "));

        apiExceptionDetail = new ApiExceptionDetail(HttpStatus.UNSUPPORTED_MEDIA_TYPE, exception.getLocalizedMessage(),
                Arrays.asList(errorBuilder.substring(0, errorBuilder.length() - 2)));
        return new ResponseEntity<Object>(apiExceptionDetail, new HttpHeaders(), apiExceptionDetail.getStatus());
    }

    // 500

    @ExceptionHandler({ ApiException.class })
    public ResponseEntity<Object> handleApiException(final ApiException ex, final WebRequest request) {
        ApiExceptionDetail apiExceptionDetail;

        apiExceptionDetail = new ApiExceptionDetail(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), Arrays.asList("error occurred"));
        return new ResponseEntity<Object>(apiExceptionDetail, new HttpHeaders(), apiExceptionDetail.getStatus());
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(final Exception ex, final WebRequest request) {
        ApiExceptionDetail apiExceptionDetail;

        apiExceptionDetail = new ApiExceptionDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(),
                Arrays.asList("error occurred"));
        return new ResponseEntity<Object>(apiExceptionDetail, new HttpHeaders(), apiExceptionDetail.getStatus());
    }
}