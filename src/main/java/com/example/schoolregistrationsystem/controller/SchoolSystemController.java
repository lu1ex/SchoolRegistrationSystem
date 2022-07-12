package com.example.schoolregistrationsystem.controller;

import com.example.schoolregistrationsystem.DTO.CourseFullDTO;
import com.example.schoolregistrationsystem.DTO.CourseDTO;
import com.example.schoolregistrationsystem.DTO.StudentFullDTO;
import com.example.schoolregistrationsystem.DTO.StudentDTO;
import com.example.schoolregistrationsystem.entity.CourseEntity;
import com.example.schoolregistrationsystem.entity.StudentEntity;
import com.example.schoolregistrationsystem.requestBodyModel.RegisterStudentRequestBodyModel;
import com.example.schoolregistrationsystem.service.ModelMapperService;
import com.example.schoolregistrationsystem.service.SchoolSystemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    @GetMapping("{schoolName}/{courseName}/students")
    public ResponseEntity<Set<StudentDTO>> getAllStudentsFromCourse(@PageableDefault(value = 10) Pageable pageable,
                                                                    @PathVariable String courseName, @PathVariable String schoolName) {
        Set<StudentEntity> studentEntities = schoolSystemService.getAllStudentsFromCourse(courseName, schoolName);
        Set<StudentDTO> studentDTOS = modelMapperService.mapSetToSetOfEnteredClass(studentEntities, StudentDTO.class);

        return new ResponseEntity<>(studentDTOS, HttpStatus.OK);
    }

    @GetMapping("/{studentCardID}/courses")
    public ResponseEntity<Set<CourseDTO>> getAllCoursersFormStudent(@PageableDefault(value = 10) Pageable pageable,
                                                                    @PathVariable String studentCardID) {
        Set<CourseEntity> courseEntities = schoolSystemService.getAllCoursesFromStudent(studentCardID);
        Set<CourseDTO> courseDTOS = modelMapperService.mapSetToSetOfEnteredClass(courseEntities, CourseDTO.class);

        return new ResponseEntity<>(courseDTOS, HttpStatus.OK);
    }

    @GetMapping("/students_without_courses")
    public ResponseEntity<Set<StudentFullDTO>> getAllStudentsWithoutCourses(@PageableDefault(value = 10) Pageable pageable) {
        Set<StudentEntity> studentEntities = schoolSystemService.getAllStudentsWithoutEnrolledCourses();
        Set<StudentFullDTO> studentFullDTOS = modelMapperService.mapSetToSetOfEnteredClass(studentEntities, StudentFullDTO.class);

        return new ResponseEntity<>(studentFullDTOS, HttpStatus.OK);
    }

    @GetMapping("/{schoolName}/courses_without_students")
    public ResponseEntity<Set<CourseFullDTO>> getAllCoursesWithoutStudents(@PathVariable String schoolName,
                                                                           @PageableDefault(value = 10) Pageable pageable) {
        Set<CourseEntity> courseEntities = schoolSystemService.getAllCoursesWithoutEnrolledStudents(schoolName);
        Set<CourseFullDTO> courseFullDTOS = modelMapperService.mapSetToSetOfEnteredClass(courseEntities, CourseFullDTO.class);

        return new ResponseEntity<>(courseFullDTOS, HttpStatus.OK);
    }
}
