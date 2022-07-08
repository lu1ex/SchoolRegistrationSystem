package com.example.schoolregistrationsystem.controller;

import com.example.schoolregistrationsystem.DTO.StudentDTO;
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
    public ResponseEntity<StudentDTO> get(@PathVariable String id) {
        StudentEntity studentEntity = studentEntityService.getStudentEntityByStudentCardID(id);
        StudentDTO studentDTO = modelMapperService.mapObjectToObjectOfEnteredClass(studentEntity, StudentDTO.class);
        return new ResponseEntity<>(studentDTO, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<Set<StudentDTO>> getAll() {
        Set<StudentEntity> studentEntitySet = studentEntityService.getAllStudentEntities();
        Set<StudentDTO> studentDTOSet = modelMapperService.mapSetToSetOfEnteredClass(studentEntitySet, StudentDTO.class);
        return new ResponseEntity<>(studentDTOSet, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<StudentDTO> post(@RequestBody @Valid StudentRequestBodyModel studentRequestBodyModel) {
        StudentEntity newStudentEntity = studentEntityService.createStudentEntity(studentRequestBodyModel);
        StudentDTO studentDTO = modelMapperService.mapObjectToObjectOfEnteredClass(newStudentEntity, StudentDTO.class);
        return new ResponseEntity<>(studentDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        studentEntityService.deleteStudentEntityById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> put(@PathVariable String id,
                                          @RequestBody @Valid StudentRequestBodyModel studentRequestBodyModel) {
        StudentEntity studentEntity = studentEntityService.putStudentEntity(id, studentRequestBodyModel);
        StudentDTO studentDTO = modelMapperService.mapObjectToObjectOfEnteredClass(studentEntity, StudentDTO.class);
        return new ResponseEntity<>(studentDTO, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<StudentDTO> patch(@PathVariable String id,
                                            @RequestBody @Valid Map<Object, Object> fields) {
        StudentEntity studentEntity = studentEntityService.patchStudentEntity(id, fields);
        StudentDTO studentDTO = modelMapperService.mapObjectToObjectOfEnteredClass(studentEntity, StudentDTO.class);
        return new ResponseEntity<>(studentDTO, HttpStatus.OK);
    }
}
