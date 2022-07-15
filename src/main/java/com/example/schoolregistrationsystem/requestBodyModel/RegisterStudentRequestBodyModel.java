package com.example.schoolregistrationsystem.requestBodyModel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name="RegisterStudentRequestBodyModel", description="Request object to register or unregister student from Course. " +
        "Fields can not be empty")
public class RegisterStudentRequestBodyModel {
    @NotEmpty(message = "Provide student card ID")
    private String studentCardID;
    @NotEmpty(message = "Provide course name")
    private String courseName;
    @NotEmpty(message = "Provide school name")
    private String schoolName;
}
