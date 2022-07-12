package com.example.schoolregistrationsystem.DTO;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class StudentDTO {
    private String name;
    private String surname;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private String studentCardID;
}
