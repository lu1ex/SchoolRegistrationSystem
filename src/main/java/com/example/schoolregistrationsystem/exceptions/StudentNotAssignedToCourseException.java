package com.example.schoolregistrationsystem.exceptions;

import static java.text.MessageFormat.format;

public class
StudentNotAssignedToCourseException extends RuntimeException {

    public StudentNotAssignedToCourseException() {
        super(format("Student is not assigned to that course"));
    }
}
