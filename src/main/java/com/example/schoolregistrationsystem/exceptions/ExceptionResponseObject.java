package com.example.schoolregistrationsystem.exceptions;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ExceptionResponseObject {
    private LocalDateTime timestamp;
    private int status;
    private String errorName;
    private String message;

    public ExceptionResponseObject(int status, String errorName, String message) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.errorName = errorName;
        this.message = message;
    }
}
