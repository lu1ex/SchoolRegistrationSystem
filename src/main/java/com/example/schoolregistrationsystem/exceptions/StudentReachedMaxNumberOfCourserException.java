package com.example.schoolregistrationsystem.exceptions;

import static java.text.MessageFormat.format;

public class StudentReachedMaxNumberOfCourserException extends RuntimeException {

    public StudentReachedMaxNumberOfCourserException() {
        super(format("Student has reached  maximum number of courses"));
    }
}
