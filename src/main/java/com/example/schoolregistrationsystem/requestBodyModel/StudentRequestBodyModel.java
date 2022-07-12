package com.example.schoolregistrationsystem.requestBodyModel;

import com.example.schoolregistrationsystem.validation.PhoneNumber;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StudentRequestBodyModel {
    @NotEmpty
    private String name;
    @NotEmpty
    private String surname;
    private LocalDate dateOfBirth;
    @PhoneNumber
    private String phoneNumber;
    @NotEmpty
    private String studentCardID; // numer legitymacji szkolnej / indeksu
}