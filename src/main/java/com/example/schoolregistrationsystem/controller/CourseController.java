package com.example.schoolregistrationsystem.controller;

import com.example.schoolregistrationsystem.DTO.CourseFullDTO;
import com.example.schoolregistrationsystem.entity.CourseEntity;
import com.example.schoolregistrationsystem.requestBodyModel.CourseRequestBodyModel;
import com.example.schoolregistrationsystem.service.CourseEntityService;
import com.example.schoolregistrationsystem.service.ModelMapperService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    public ResponseEntity<CourseFullDTO> get(@PathVariable String id) {
        CourseEntity courseEntity = courseEntityService.getCourseEntityById(id);
        CourseFullDTO courseFullDTO = modelMapperService.mapObjectToObjectOfEnteredClass(courseEntity, CourseFullDTO.class);
        return new ResponseEntity<>(courseFullDTO, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<Set<CourseFullDTO>> getAll(@PageableDefault(value = 10) Pageable pageable) {
        Set<CourseEntity> courseEntitySet = courseEntityService.getAllCoursesEntities();
        Set<CourseFullDTO> courseFullDTOSet = modelMapperService.mapSetToSetOfEnteredClass(courseEntitySet, CourseFullDTO.class);
        return new ResponseEntity<>(courseFullDTOSet, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CourseFullDTO> post(@RequestBody @Valid CourseRequestBodyModel courseRequestBodyModel) {
        CourseEntity newCourseEntity = courseEntityService.createCourseEntity(courseRequestBodyModel);
        CourseFullDTO courseFullDTO = modelMapperService.mapObjectToObjectOfEnteredClass(newCourseEntity, CourseFullDTO.class);
        return new ResponseEntity<>(courseFullDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        courseEntityService.deleteCourseEntityById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseFullDTO> put(@PathVariable String id,
                                             @RequestBody @Valid CourseRequestBodyModel courseRequestBodyModel) {
        CourseEntity courseEntity = courseEntityService.putCourseEntity(id, courseRequestBodyModel);
        CourseFullDTO courseFullDTO = modelMapperService.mapObjectToObjectOfEnteredClass(courseEntity, CourseFullDTO.class);
        return new ResponseEntity<>(courseFullDTO, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CourseFullDTO> patch(@PathVariable String id,
                                               @RequestBody @Valid Map<Object, Object> fields) {
        CourseEntity courseEntity = courseEntityService.patchCourseEntity(id, fields);
        CourseFullDTO courseFullDTO = modelMapperService.mapObjectToObjectOfEnteredClass(courseEntity, CourseFullDTO.class);
        return new ResponseEntity<>(courseFullDTO, HttpStatus.OK);
    }
}
