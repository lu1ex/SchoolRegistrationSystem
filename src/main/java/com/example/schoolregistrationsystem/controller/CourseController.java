package com.example.schoolregistrationsystem.controller;

import com.example.schoolregistrationsystem.DTO.CourseFullDTO;
import com.example.schoolregistrationsystem.DTO.StudentFullDTO;
import com.example.schoolregistrationsystem.entity.CourseEntity;
import com.example.schoolregistrationsystem.requestBodyModel.CourseRequestBodyModel;
import com.example.schoolregistrationsystem.requestBodyModel.StudentRequestBodyModel;
import com.example.schoolregistrationsystem.service.CourseEntityService;
import com.example.schoolregistrationsystem.service.ModelMapperService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
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

import javax.validation.Valid;
import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/course")
public class CourseController {
    private final CourseEntityService courseEntityService;
    private final ModelMapperService modelMapperService;

    @Operation(summary = "Get Course by ID", description = "Get complete course object from Database with set of courses",
            parameters = {@Parameter(in = ParameterIn.PATH, name = "id", description = "Unique identifier from DB")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the Course. Returns CourseFullDTO",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CourseFullDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Object with id not found. Thrown ValueNotFoundException. <br />" +
                    "Returns ExceptionResponseObject", content = @Content)})
    @GetMapping("/{id}")
    public ResponseEntity<CourseFullDTO> get(@PathVariable String id) {
        CourseEntity courseEntity = courseEntityService.getCourseEntityById(id);
        CourseFullDTO courseFullDTO = modelMapperService.mapObjectToObjectOfEnteredClass(courseEntity, CourseFullDTO.class);
        return new ResponseEntity<>(courseFullDTO, HttpStatus.OK);
    }

    @Operation(summary = "Get all Courses from DB", description = "Get all courses objects from Database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns Set of CourseFullDTO objects",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CourseFullDTO.class)))})})
    @GetMapping("/all")
    public ResponseEntity<Set<CourseFullDTO>> getAll(@PageableDefault(value = 10) Pageable pageable) {
        Set<CourseEntity> courseEntitySet = courseEntityService.getAllCoursesEntities();
        Set<CourseFullDTO> courseFullDTOSet = modelMapperService.mapSetToSetOfEnteredClass(courseEntitySet, CourseFullDTO.class);
        return new ResponseEntity<>(courseFullDTOSet, HttpStatus.OK);
    }

    @Operation(summary = "Add new Course", description = "Create new CourseEntity and save in Database by sanding " +
            "CourseRequestBodyModel object",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = CourseRequestBodyModel.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created CourseEntity and saved in Database. " +
                    "Returns CourseFullDTO",
                    content = {@Content(schema = @Schema(implementation = CourseFullDTO.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", description = "Possible reasons: " +
                    "<ul><li>CourseRequestBodyModel with incorrect fields. " +
                    "Thrown MethodArgumentNotValidException. Returns Possible reasons: " +
                    "<ol type=\"A\"><li>Name can not be empty</li>" +
                    "<li>SchoolName can not be empty</li</ol></li></ul>" +
                    "<ul><li>There is already courser with that name in that school. " +
                    "</li></ul>Returns ExceptionResponseObject", content = @Content)})
    @PostMapping
    public ResponseEntity<CourseFullDTO> post(@RequestBody @Valid CourseRequestBodyModel courseRequestBodyModel) {
        CourseEntity newCourseEntity = courseEntityService.createCourseEntity(courseRequestBodyModel);
        CourseFullDTO courseFullDTO = modelMapperService.mapObjectToObjectOfEnteredClass(newCourseEntity, CourseFullDTO.class);
        return new ResponseEntity<>(courseFullDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "Delete Course by ID", description = "Delete curse from Database",
            parameters = {@Parameter(in = ParameterIn.PATH, name = "id", description = "Unique identifier from DB")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the Course and deleted from Database",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Object with id not found. Returns ExceptionResponseObject" +
                    " Thrown ValueNotFoundException. <br />", content = @Content)})
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        courseEntityService.deleteCourseEntityById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Put Course by ID", description = "Update every field of Course and save in Database",
            parameters = {@Parameter(in = ParameterIn.PATH, name = "id", description = "Unique identifier from DB")},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = CourseRequestBodyModel.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the Course. Updated fields and saved in Database",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CourseFullDTO.class))}),
            @ApiResponse(responseCode = "400", description = "<ul><li>CourseRequestBodyModel with incorrect fields. " +
                    "Thrown MethodArgumentNotValidException. Possible reasons:<ol type=\"A\"><li>Name can not be empty</li>" +
                    "<li>SchoolName can not be empty</li></ol></ul>", content = @Content),
            @ApiResponse(responseCode = "404", description = "Object with id not found. Returns ExceptionResponseObject" +
                    " Thrown ValueNotFoundException. <br />", content = @Content)})
    @PutMapping("/{id}")
    public ResponseEntity<CourseFullDTO> put(@PathVariable String id,
                                             @RequestBody @Valid CourseRequestBodyModel courseRequestBodyModel) {
        CourseEntity courseEntity = courseEntityService.putCourseEntity(id, courseRequestBodyModel);
        CourseFullDTO courseFullDTO = modelMapperService.mapObjectToObjectOfEnteredClass(courseEntity, CourseFullDTO.class);
        return new ResponseEntity<>(courseFullDTO, HttpStatus.OK);
    }

    @Operation(summary = "Patch Course by ID", description = "Update only provided fields of Course and save in Database." +
            "If provide incorrect name of field will change nothing and return status 200 - OK.",
            parameters = {@Parameter(in = ParameterIn.PATH, name = "id", description = "Unique identifier from DB")},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(ref = "#/components/schemas/Map"))}))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the Course. Updated fields and saved in Database",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CourseFullDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Field was entered in an illegal type.  Returns " +
                    "ExceptionResponseObject <br /> Thrown IllegalArgumentException", content = @Content),
            @ApiResponse(responseCode = "404", description = "Object with id not found. Returns ExceptionResponseObject" +
                    " Thrown ValueNotFoundException. <br />", content = @Content)})
    @PatchMapping("/{id}")
    public ResponseEntity<CourseFullDTO> patch(@PathVariable String id,
                                               @RequestBody @Valid Map<Object, Object> fields) {
        CourseEntity courseEntity = courseEntityService.patchCourseEntity(id, fields);
        CourseFullDTO courseFullDTO = modelMapperService.mapObjectToObjectOfEnteredClass(courseEntity, CourseFullDTO.class);
        return new ResponseEntity<>(courseFullDTO, HttpStatus.OK);
    }
}
