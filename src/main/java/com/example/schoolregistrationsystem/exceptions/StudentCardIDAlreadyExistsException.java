package com.example.schoolregistrationsystem.exceptions;

import static java.text.MessageFormat.format;

public class StudentCardIDAlreadyExistsException extends RuntimeException {

    public StudentCardIDAlreadyExistsException() {
        super(format("There is already student with that student card ID."));
    }
}

