package com.example.schoolregistrationsystem.exceptions;

import static java.text.MessageFormat.format;

public class ValueNotFoundException extends RuntimeException {

    public ValueNotFoundException(String id) {
        super(format("Object with id {0} not found", id));
    }
}
