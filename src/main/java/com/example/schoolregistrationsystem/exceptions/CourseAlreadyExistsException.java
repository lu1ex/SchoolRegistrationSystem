package com.example.schoolregistrationsystem.exceptions;

import static java.text.MessageFormat.format;

public class CourseAlreadyExistsException extends RuntimeException {

    public CourseAlreadyExistsException() {
        super(format("There is already courser with that name in that school."));
    }
}