package com.example.schoolregistrationsystem.controller;

import com.example.schoolregistrationsystem.DTO.CourseFullDTO;
import com.example.schoolregistrationsystem.DTO.CourseDTO;
import com.example.schoolregistrationsystem.DTO.StudentFullDTO;
import com.example.schoolregistrationsystem.DTO.StudentDTO;
import com.example.schoolregistrationsystem.entity.CourseEntity;
import com.example.schoolregistrationsystem.entity.StudentEntity;
import com.example.schoolregistrationsystem.requestBodyModel.CourseRequestBodyModel;
import com.example.schoolregistrationsystem.requestBodyModel.RegisterStudentRequestBodyModel;
import com.example.schoolregistrationsystem.service.ModelMapperService;
import com.example.schoolregistrationsystem.service.SchoolSystemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/system")
public class SchoolSystemController {
    private final ModelMapperService modelMapperService;
    private final SchoolSystemService schoolSystemService;


    @Operation(summary = "Register Student on Course", description = "Student can enroll on course by sending infomration" +
            "about student card ID, name of course and name of school.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = RegisterStudentRequestBodyModel.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the Student and Course. " +
                    "The Student and the Course have not reached the limit of participants. Student enrolled to Course",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = String.class), examples = @ExampleObject(
                            value = "{Student registered to course}"))}),
            @ApiResponse(responseCode = "400", description = "Possible reasons:" +
                    "<ul>" +
                    "<li>Student already assigned to Course. Thrown StudentAlreadyAssignedToCourseException</li>" +
                    "<li>Student enrolled to maximum number of courses. Thrown StudentReachedMaxNumberOfCourserException</li>" +
                    "<li>Course reached maximum number of Students. Thrown CourseReachedMaxNumberOfStudentsException.</li>" +
                    "</ul>Returns ExceptionResponseObject", content = @Content),
            @ApiResponse(responseCode = "404", description = "Possible reasons:" +
                    "<ul><li>Student not found by student card ID. Thrown ValueNotFoundException</li>" +
                    "<li>Course not found by course name and school name. Thrown ValueNotFoundException" +
                    "</li></ul>Returns ExceptionResponseObject",
                    content = @Content)})
    @PostMapping("/register")
    public ResponseEntity<String> registerStudentToCourse(@RequestBody RegisterStudentRequestBodyModel requestBodyModel) {
        String response = schoolSystemService.registerStudentToCourse(requestBodyModel.getStudentCardID(),
                requestBodyModel.getCourseName(), requestBodyModel.getSchoolName());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Unregister Student from Course", description = "Student can unregister from course " +
            "by sending information about student card ID, name of course and name of school.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = RegisterStudentRequestBodyModel.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the Student and Course. Student unregistered from Course",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = String.class), examples = @ExampleObject(
                            value = "{Student unregistered from course}"))}),
            @ApiResponse(responseCode = "400", description = "Student was not enrolled to course." +
                    "Thrown StudentNotAssignedToCourseException. Returns ExceptionResponseObject", content = @Content),
            @ApiResponse(responseCode = "404", description = "Possible reasons:" +
                    "<ul><li>Student not found by student card ID. Thrown ValueNotFoundException</li>" +
                    "<li>Course not found by course name and school name. Thrown ValueNotFoundException" +
                    "</li></ul>Returns ExceptionResponseObject",
                    content = @Content)})
    @PostMapping("/unregister")
    public ResponseEntity<String> unregisterStudentFromCourse(@RequestBody RegisterStudentRequestBodyModel requestBodyModel) {
        String response = schoolSystemService.unregisterStudentFromCourse(requestBodyModel.getStudentCardID(),
                requestBodyModel.getCourseName(), requestBodyModel.getSchoolName());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Get all Students enrolled on course", description = "Get all students enrolled to course" +
            "from a given school.", parameters = {@Parameter(in = ParameterIn.PATH, name = "schoolName",
            description = "Name of school"), @Parameter(in = ParameterIn.PATH, name = "courseName",
            description = "Name of course")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns Set of StudentDTO objects",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = StudentDTO.class)))}),
            @ApiResponse(responseCode = "404", description = "Course not exists in that school. " +
                    "Thrown ValueNotFoundException. Returns ExceptionResponseObject", content = @Content)})
    @GetMapping("{schoolName}/{courseName}/students")
    public ResponseEntity<Set<StudentDTO>> getAllStudentsFromCourse(@PageableDefault(value = 10) Pageable pageable,
                                                                    @PathVariable String courseName, @PathVariable String schoolName) {
        Set<StudentEntity> studentEntities = schoolSystemService.getAllStudentsFromCourse(courseName, schoolName);
        Set<StudentDTO> studentDTOS = modelMapperService.mapSetToSetOfEnteredClass(studentEntities, StudentDTO.class);

        return new ResponseEntity<>(studentDTOS, HttpStatus.OK);
    }

    @Operation(summary = "Get all courses for Student", description = "Get all the Courses the Student is enrolled in",
            parameters = {@Parameter(in = ParameterIn.PATH, name = "studentCardID",
            description = "Unique student card ID")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns Set of CourseDTO objects",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CourseDTO.class)))}),
            @ApiResponse(responseCode = "404", description = "Student not exists. Thrown ValueNotFoundException. " +
                    "Returns ExceptionResponseObject", content = @Content)})
    @GetMapping("/{studentCardID}/courses")
    public ResponseEntity<Set<CourseDTO>> getAllCoursersFormStudent(@PageableDefault(value = 10) Pageable pageable,
                                                                    @PathVariable String studentCardID) {
        Set<CourseEntity> courseEntities = schoolSystemService.getAllCoursesFromStudent(studentCardID);
        Set<CourseDTO> courseDTOS = modelMapperService.mapSetToSetOfEnteredClass(courseEntities, CourseDTO.class);

        return new ResponseEntity<>(courseDTOS, HttpStatus.OK);
    }

    @Operation(summary = "Get all students without any courses", description = "Get all students without any courses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns Set of StudentFullDTO objects",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = StudentFullDTO.class)))})})
    @GetMapping("/students_without_courses")
    public ResponseEntity<Set<StudentFullDTO>> getAllStudentsWithoutCourses(@PageableDefault(value = 10) Pageable pageable) {
        Set<StudentEntity> studentEntities = schoolSystemService.getAllStudentsWithoutEnrolledCourses();
        Set<StudentFullDTO> studentFullDTOS = modelMapperService.mapSetToSetOfEnteredClass(studentEntities, StudentFullDTO.class);

        return new ResponseEntity<>(studentFullDTOS, HttpStatus.OK);
    }

    @Operation(summary = "Get Courses from school without any students", description = "Get Courses from school without " +
            "any students", parameters = {@Parameter(in = ParameterIn.PATH, name = "schoolName", description = "Name of school")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns Set of CourseFullDTO objects",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CourseFullDTO.class)))})})
    @GetMapping("/{schoolName}/courses_without_students")
    public ResponseEntity<Set<CourseFullDTO>> getAllCoursesWithoutStudents(@PathVariable String schoolName,
                                                                           @PageableDefault(value = 10) Pageable pageable) {
        Set<CourseEntity> courseEntities = schoolSystemService.getAllCoursesWithoutEnrolledStudents(schoolName);
        Set<CourseFullDTO> courseFullDTOS = modelMapperService.mapSetToSetOfEnteredClass(courseEntities, CourseFullDTO.class);

        return new ResponseEntity<>(courseFullDTOS, HttpStatus.OK);
    }
}
