package com.example.schoolregistrationsystem.DTO;

import com.example.schoolregistrationsystem.entity.CourseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentDTO {
    private String name;
    private String surname;
    private Date dateOfBirth;
    private String phoneNumber;
    private String studentCardID;
    private Set<CourseEntity> courses = new HashSet<>();
}
