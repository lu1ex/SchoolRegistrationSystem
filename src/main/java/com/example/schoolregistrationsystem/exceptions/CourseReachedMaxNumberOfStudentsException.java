package com.example.schoolregistrationsystem.exceptions;

import static java.text.MessageFormat.format;

public class CourseReachedMaxNumberOfStudentsException extends RuntimeException{
    public CourseReachedMaxNumberOfStudentsException() {
        super(format("Course has reached maximum number of students"));
    }
}
