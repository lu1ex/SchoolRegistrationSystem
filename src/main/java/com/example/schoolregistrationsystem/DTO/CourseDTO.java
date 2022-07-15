package com.example.schoolregistrationsystem.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(name="CourseDTO", description="Model of Course from Database without Set of registered Students")
public class CourseDTO {
    private String name;
    private String schoolName;
}
