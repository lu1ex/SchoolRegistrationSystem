package com.example.schoolregistrationsystem.controller;

import com.example.schoolregistrationsystem.DTO.StudentFullDTO;
import com.example.schoolregistrationsystem.entity.StudentEntity;
import com.example.schoolregistrationsystem.requestBodyModel.StudentRequestBodyModel;
import com.example.schoolregistrationsystem.service.ModelMapperService;
import com.example.schoolregistrationsystem.service.StudentEntityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/student")
public class StudentController {
    private final StudentEntityService studentEntityService;
    private final ModelMapperService modelMapperService;

    @Operation(summary = "Get Student by ID", description = "Get complete student object from Database with set of courses",
            parameters = {@Parameter(in = ParameterIn.PATH, name = "id", description = "Unique identifier from DB")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the Student. Returns StudentFullDTO",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = StudentFullDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Object with id not found. Thrown ValueNotFoundException. <br />" +
                    "Returns ExceptionResponseObject", content = @Content)})
    @GetMapping("/{id}")
    public ResponseEntity<StudentFullDTO> get(@PathVariable String id) {
        StudentEntity studentEntity = studentEntityService.getStudentEntityById(id);
        StudentFullDTO studentFullDTO = modelMapperService.mapObjectToObjectOfEnteredClass(studentEntity, StudentFullDTO.class);
        return new ResponseEntity<>(studentFullDTO, HttpStatus.OK);
    }

    @Operation(summary = "Get all Students from DB", description = "Get all students objects from Database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns Set of StudentFullDTO objects",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = StudentFullDTO.class)))})})
    @GetMapping("/all")
    public ResponseEntity<Set<StudentFullDTO>> getAll() {
        Set<StudentEntity> studentEntitySet = studentEntityService.getAllStudentEntities();
        Set<StudentFullDTO> studentFullDTOSet = modelMapperService.mapSetToSetOfEnteredClass(studentEntitySet, StudentFullDTO.class);
        return new ResponseEntity<>(studentFullDTOSet, HttpStatus.OK);
    }

    @Operation(summary = "Add new Student", description = "Create new StudentEntity and save in Database by sanding " +
            "StudentRequestBodyModel object",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = StudentRequestBodyModel.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created StudentEntity and saved in Database. " +
                    "Returns StudentFullDTO",
                    content = {@Content(schema = @Schema(implementation = StudentFullDTO.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", description = "Possible reasons: <ul><li>StudentRequestBodyModel " +
                    "with incorrect fields. Thrown MethodArgumentNotValidException. Possible reasons: " +
                    "in message: <ol type=\"A\"><li>Name can not be empty</li>" +
                    "<li>Only 9 numbers format allowed</li> <li>Surname can not be empty</li</ol>" +
                    "</li></ul>" +
                    "<ul><li>There is already student with that student card ID. <br /> " +
                    "Thrown StudentCardIDAlreadyExistsException</li></ul>" +
                    "Returns ExceptionResponseObject. ", content = @Content)})
    @PostMapping
    public ResponseEntity<StudentFullDTO> post(@RequestBody @Valid StudentRequestBodyModel studentRequestBodyModel) {
        StudentEntity newStudentEntity = studentEntityService.createStudentEntity(studentRequestBodyModel);
        StudentFullDTO studentFullDTO = modelMapperService.mapObjectToObjectOfEnteredClass(newStudentEntity, StudentFullDTO.class);
        return new ResponseEntity<>(studentFullDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "Delete Student by ID", description = "Delete student from Database",
            parameters = {@Parameter(in = ParameterIn.PATH, name = "id", description = "Unique identifier from DB")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the Student and deleted from Database",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Object with id not found. Returns ExceptionResponseObject" +
                    " Thrown ValueNotFoundException. <br />", content = @Content)})
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        studentEntityService.deleteStudentEntityById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Put Student by ID", description = "Update every field of Student and save in Database",
            parameters = {@Parameter(in = ParameterIn.PATH, name = "id", description = "Unique identifier from DB")},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
                    schema = @Schema(implementation = StudentRequestBodyModel.class), mediaType = MediaType.APPLICATION_JSON_VALUE)))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the Student. Updated fields and saved in Database",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = StudentFullDTO.class))}),
            @ApiResponse(responseCode = "400", description = "<ul><li>StudentRequestBodyModel with incorrect fields. " +
                    "Thrown MethodArgumentNotValidException. Possible reasons: in message: " +
                    "<ol type=\"A\"><li>Name can not be empty</li>" +
                    "<li>Only 9 numbers format allowed</li> <li>Surname can not be empty</li</ol></ul>", content = @Content),
            @ApiResponse(responseCode = "404", description = "Object with id not found. Returns ExceptionResponseObject" +
                    " Thrown ValueNotFoundException. <br />", content = @Content)})
    @PutMapping("/{id}")
    public ResponseEntity<StudentFullDTO> put(@PathVariable String id,
                                              @RequestBody @Valid StudentRequestBodyModel studentRequestBodyModel) {
        StudentEntity studentEntity = studentEntityService.putStudentEntity(id, studentRequestBodyModel);
        StudentFullDTO studentFullDTO = modelMapperService.mapObjectToObjectOfEnteredClass(studentEntity, StudentFullDTO.class);
        return new ResponseEntity<>(studentFullDTO, HttpStatus.OK);
    }

    @Operation(summary = "Patch Student by ID", description = "Update only provided fields of Student and save in Database." +
            "If provide incorrect name of field will change nothing and return status 200 - OK.",
            parameters = {@Parameter(in = ParameterIn.PATH, name = "id", description = "Unique identifier from DB")},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = {@Content(mediaType =
                    MediaType.APPLICATION_JSON_VALUE, schema = @Schema(ref = "#/components/schemas/Map"))}))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the Student. Updated fields and saved in Database",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = StudentFullDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Field was entered in an illegal type.  Returns " +
                    "ExceptionResponseObject <br /> Thrown IllegalArgumentException", content = @Content),
            @ApiResponse(responseCode = "404", description = "Object with id not found. Returns ExceptionResponseObject" +
                    " Thrown ValueNotFoundException. <br />", content = @Content)})
    @PatchMapping("/{id}")
    public ResponseEntity<StudentFullDTO> patch(@PathVariable String id,
                                                @RequestBody @Valid Map<Object, Object> fields) {
        StudentEntity studentEntity = studentEntityService.patchStudentEntity(id, fields);
        StudentFullDTO studentFullDTO = modelMapperService.mapObjectToObjectOfEnteredClass(studentEntity, StudentFullDTO.class);
        return new ResponseEntity<>(studentFullDTO, HttpStatus.OK);
    }
}
