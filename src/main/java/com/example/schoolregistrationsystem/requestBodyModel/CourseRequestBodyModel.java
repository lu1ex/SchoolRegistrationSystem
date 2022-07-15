package com.example.schoolregistrationsystem.requestBodyModel;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CourseRequestBodyModel {
    @NotEmpty(message = "Name can not be empty")
    private String name;
    @NotEmpty(message = "SchoolName can not be empty")
    private String schoolName;
}
