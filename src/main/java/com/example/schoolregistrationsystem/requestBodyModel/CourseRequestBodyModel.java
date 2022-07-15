package com.example.schoolregistrationsystem.requestBodyModel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Schema(name="CourseRequestBodyModel", description="Request object to create or change Course. Fields can not be empty")
public class CourseRequestBodyModel {
    @NotEmpty(message = "Name can not be empty")
    private String name;
    @NotEmpty(message = "SchoolName can not be empty")
    private String schoolName;
}
