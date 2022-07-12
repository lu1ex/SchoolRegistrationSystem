package com.example.schoolregistrationsystem.DTO;

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
public class CourseFullDTO {
    private String name;
    private String schoolName;
    private Set<StudentFullDTO> students = new HashSet<>();
}
