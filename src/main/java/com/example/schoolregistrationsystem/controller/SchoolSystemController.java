package com.example.schoolregistrationsystem.controller;

import com.example.schoolregistrationsystem.DTO.CourseDTO;
import com.example.schoolregistrationsystem.DTO.StudentDTO;
import com.example.schoolregistrationsystem.entity.CourseEntity;
import com.example.schoolregistrationsystem.entity.StudentEntity;
import com.example.schoolregistrationsystem.requestBodyModel.RegisterStudentRequestBodyModel;
import com.example.schoolregistrationsystem.service.ModelMapperService;
import com.example.schoolregistrationsystem.service.SchoolSystemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/system")
public class SchoolSystemController {
    private final ModelMapperService modelMapperService;
    private final SchoolSystemService schoolSystemService;

    @PostMapping("/register")
    public ResponseEntity<String> registerStudentToCourse(@RequestBody RegisterStudentRequestBodyModel requestBodyModel) {
        String response = schoolSystemService.registerStudentToCourse(requestBodyModel.getStudentCardID(),
                requestBodyModel.getCourseName(), requestBodyModel.getSchoolName());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/unregister")
    public ResponseEntity<String> unregisterStudentFromCourse(@RequestBody RegisterStudentRequestBodyModel requestBodyModel) {
        String response = schoolSystemService.unregisterStudentFromCourse(requestBodyModel.getStudentCardID(),
                requestBodyModel.getCourseName(), requestBodyModel.getSchoolName());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{courseName}/students")
    public ResponseEntity<Set<StudentDTO>> getAllStudentsFromCourse(@PathVariable String courseName) {
        Set<StudentEntity> studentEntities = schoolSystemService.getAllStudentsFromCourse(courseName);
        Set<StudentDTO> studentDTOS = modelMapperService.mapSetToSetOfEnteredClass(studentEntities, StudentDTO.class);

        return new ResponseEntity<>(studentDTOS, HttpStatus.OK);
    }

    @GetMapping("/{studentCardID}/courses")
    public ResponseEntity<Set<CourseDTO>> getAllCoursersFormStudent(@PathVariable String studentCardID) {
        Set<CourseEntity> courseEntities = schoolSystemService.getAllCoursesFromStudent(studentCardID);
        Set<CourseDTO> courseDTOS = modelMapperService.mapSetToSetOfEnteredClass(courseEntities, CourseDTO.class);

        return new ResponseEntity<>(courseDTOS, HttpStatus.OK);
    }

    @GetMapping("/courses_without_students")
    public ResponseEntity<Set<CourseDTO>> getAllCoursesWithoutStudents() {
        Set<CourseEntity> courseEntities = schoolSystemService.getAllCoursesWithoutEnrolledStudents();
        Set<CourseDTO> courseDTOS = modelMapperService.mapSetToSetOfEnteredClass(courseEntities, CourseDTO.class);

        return new ResponseEntity<>(courseDTOS, HttpStatus.OK);
    }

    @GetMapping("/students_without_courses")
    public ResponseEntity<Set<StudentDTO>> getAllStudentsWithoutCourses() {
        Set<StudentEntity> studentEntities = schoolSystemService.getAllStudentsWithoutEnrolledCourses();
        Set<StudentDTO> studentDTOS = modelMapperService.mapSetToSetOfEnteredClass(studentEntities, StudentDTO.class);

        return new ResponseEntity<>(studentDTOS, HttpStatus.OK);
    }
}
