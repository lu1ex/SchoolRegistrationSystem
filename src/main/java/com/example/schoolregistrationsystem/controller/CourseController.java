package com.example.schoolregistrationsystem.controller;

import com.example.schoolregistrationsystem.DTO.CourseDTO;
import com.example.schoolregistrationsystem.entity.CourseEntity;
import com.example.schoolregistrationsystem.requestBodyModel.CourseRequestBodyModel;
import com.example.schoolregistrationsystem.service.CourseEntityService;
import com.example.schoolregistrationsystem.service.ModelMapperService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> get(@PathVariable String id) {
        CourseEntity courseEntity = courseEntityService.getCourseEntityById(id);
        CourseDTO courseDTO = modelMapperService.mapObjectToObjectOfEnteredClass(courseEntity, CourseDTO.class);
        return new ResponseEntity<>(courseDTO, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<Set<CourseDTO>> getAll() {
        Set<CourseEntity> courseEntitySet = courseEntityService.getAllCoursesEntities();
        Set<CourseDTO> courseDTOSet = modelMapperService.mapSetToSetOfEnteredClass(courseEntitySet, CourseDTO.class);
        return new ResponseEntity<>(courseDTOSet, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CourseDTO> post(@RequestBody @Valid CourseRequestBodyModel courseRequestBodyModel) {
        CourseEntity newCourseEntity = courseEntityService.createCourseEntity(courseRequestBodyModel);
        CourseDTO courseDTO = modelMapperService.mapObjectToObjectOfEnteredClass(newCourseEntity, CourseDTO.class);
        return new ResponseEntity<>(courseDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        courseEntityService.deleteCourseEntityById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDTO> put(@PathVariable String id,
                                          @RequestBody @Valid CourseRequestBodyModel courseRequestBodyModel) {
        CourseEntity courseEntity = courseEntityService.putCourseEntity(id, courseRequestBodyModel);
        CourseDTO courseDTO = modelMapperService.mapObjectToObjectOfEnteredClass(courseEntity, CourseDTO.class);
        return new ResponseEntity<>(courseDTO, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CourseDTO> patch(@PathVariable String id,
                                            @RequestBody @Valid Map<Object, Object> fields) {
        CourseEntity courseEntity = courseEntityService.patchCourseEntity(id, fields);
        CourseDTO courseDTO = modelMapperService.mapObjectToObjectOfEnteredClass(courseEntity, CourseDTO.class);
        return new ResponseEntity<>(courseDTO, HttpStatus.OK);
    }
}
