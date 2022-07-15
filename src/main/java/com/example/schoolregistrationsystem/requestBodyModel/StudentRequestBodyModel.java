package com.example.schoolregistrationsystem.requestBodyModel;

import com.example.schoolregistrationsystem.validation.PhoneNumber;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Schema(name="StudentRequestBodyModel", description="Request object to create or change Student. Fields can not be empty. " +
        "Only 9 numbers phone number format allowed.")
public class StudentRequestBodyModel {
    @NotEmpty(message = "Name can not be empty")
    private String name;
    @NotEmpty(message = "Surname can not be empty")
    private String surname;
    private LocalDate dateOfBirth;
    @PhoneNumber(message = "Only 9 numbers format allowed")
    private String phoneNumber;
    @NotEmpty(message = "Student card ID can not be empty")
    private String studentCardID;
}