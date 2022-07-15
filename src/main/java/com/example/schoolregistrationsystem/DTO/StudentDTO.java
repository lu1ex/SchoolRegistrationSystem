package com.example.schoolregistrationsystem.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(name="StudentDTO", description="Model of Student from Database without Set of enrolled courses")
public class StudentDTO {
    private String name;
    private String surname;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private String studentCardID;
}
