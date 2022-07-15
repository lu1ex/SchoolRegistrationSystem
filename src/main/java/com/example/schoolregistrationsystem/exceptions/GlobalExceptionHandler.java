package com.example.schoolregistrationsystem.exceptions;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ValueNotFoundException.class})
    public ResponseEntity<Object> handleExceptions(Exception ex) {
        return new ResponseEntity<>(new ExceptionResponseObject(
                404, ex.getClass().getSimpleName(), ex.getLocalizedMessage()),
                new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<Object> handleRuntimeException(Exception ex) {
        return new ResponseEntity<>(new ExceptionResponseObject(
                400, ex.getClass().getSimpleName(), ex.getLocalizedMessage()),
                new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status,
            WebRequest request) {

        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
        ExceptionResponseObject exceptionResponseObject = new ExceptionResponseObject(
                status.value(), "MethodArgumentNotValidException", errors.toString());
        /*Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());



        body.put("errors", errors);
*/
        return new ResponseEntity<>(exceptionResponseObject, headers, status);
    }
}
