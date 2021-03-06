package com.example.schoolregistrationsystem.DTO;

import com.example.schoolregistrationsystem.entity.CourseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(name="StudentFullDTO", description="Model of Student from Database withSet of enrolled courses")
public class StudentFullDTO {
    private String name;
    private String surname;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private String studentCardID;
    private Set<CourseDTO> courses = new HashSet<>();
}
