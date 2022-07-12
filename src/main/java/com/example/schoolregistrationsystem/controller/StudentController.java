package com.example.schoolregistrationsystem.controller;

import com.example.schoolregistrationsystem.DTO.StudentFullDTO;
import com.example.schoolregistrationsystem.entity.StudentEntity;
import com.example.schoolregistrationsystem.requestBodyModel.StudentRequestBodyModel;
import com.example.schoolregistrationsystem.service.ModelMapperService;
import com.example.schoolregistrationsystem.service.StudentEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/{id}")
    public ResponseEntity<StudentFullDTO> get(@PathVariable String id) {
        StudentEntity studentEntity = studentEntityService.getStudentEntityById(id);
        StudentFullDTO studentFullDTO = modelMapperService.mapObjectToObjectOfEnteredClass(studentEntity, StudentFullDTO.class);
        return new ResponseEntity<>(studentFullDTO, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<Set<StudentFullDTO>> getAll() {
        Set<StudentEntity> studentEntitySet = studentEntityService.getAllStudentEntities();
        Set<StudentFullDTO> studentFullDTOSet = modelMapperService.mapSetToSetOfEnteredClass(studentEntitySet, StudentFullDTO.class);
        return new ResponseEntity<>(studentFullDTOSet, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<StudentFullDTO> post(@RequestBody @Valid StudentRequestBodyModel studentRequestBodyModel) {
        StudentEntity newStudentEntity = studentEntityService.createStudentEntity(studentRequestBodyModel);
        StudentFullDTO studentFullDTO = modelMapperService.mapObjectToObjectOfEnteredClass(newStudentEntity, StudentFullDTO.class);
        return new ResponseEntity<>(studentFullDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        studentEntityService.deleteStudentEntityById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentFullDTO> put(@PathVariable String id,
                                              @RequestBody @Valid StudentRequestBodyModel studentRequestBodyModel) {
        StudentEntity studentEntity = studentEntityService.putStudentEntity(id, studentRequestBodyModel);
        StudentFullDTO studentFullDTO = modelMapperService.mapObjectToObjectOfEnteredClass(studentEntity, StudentFullDTO.class);
        return new ResponseEntity<>(studentFullDTO, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<StudentFullDTO> patch(@PathVariable String id,
                                                @RequestBody @Valid Map<Object, Object> fields) {
        StudentEntity studentEntity = studentEntityService.patchStudentEntity(id, fields);
        StudentFullDTO studentFullDTO = modelMapperService.mapObjectToObjectOfEnteredClass(studentEntity, StudentFullDTO.class);
        return new ResponseEntity<>(studentFullDTO, HttpStatus.OK);
    }
}
