package com.example.schoolregistrationsystem.requestBodyModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterStudentRequestBodyModel {
    private String studentCardID;
    private String courseName;
    private String schoolName;
}
