package com.example.schoolregistrationsystem.requestBodyModel;

import com.example.schoolregistrationsystem.validation.PhoneNumber;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StudentRequestBodyModel {
    @NotEmpty(message = "Name can not be empty")
    private String name;
    @NotEmpty(message = "Surname can not be empty")
    private String surname;
    private LocalDate dateOfBirth;
    @PhoneNumber(message = "Only 9 numbers format allowed")
    private String phoneNumber;
    @NotEmpty(message = "Student card ID can not be empty")
    private String studentCardID; // numer legitymacji szkolnej / indeksu
}