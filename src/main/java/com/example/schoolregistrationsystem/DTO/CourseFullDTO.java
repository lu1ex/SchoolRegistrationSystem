package com.example.schoolregistrationsystem.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(name="CourseFullDTO", description="Model of Course from Database with Set of registered students")
public class CourseFullDTO {
    private String name;
    private String schoolName;
    private Set<StudentDTO> students = new HashSet<>();
}
