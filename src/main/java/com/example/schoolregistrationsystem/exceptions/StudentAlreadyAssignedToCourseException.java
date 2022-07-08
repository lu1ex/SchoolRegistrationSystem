package com.example.schoolregistrationsystem.exceptions;

import static java.text.MessageFormat.format;

public class StudentAlreadyAssignedToCourseException extends RuntimeException {
    public StudentAlreadyAssignedToCourseException() {
        super(format("Student already assigned to that course"));
    }
}
