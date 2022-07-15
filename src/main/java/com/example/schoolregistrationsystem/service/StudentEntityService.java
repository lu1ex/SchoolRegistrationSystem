package com.example.schoolregistrationsystem.service;

import com.example.schoolregistrationsystem.entity.StudentEntity;
import com.example.schoolregistrationsystem.exceptions.StudentCardIDAlreadyExistsException;
import com.example.schoolregistrationsystem.exceptions.ValueNotFoundException;
import com.example.schoolregistrationsystem.repository.StudentEntityRepository;
import com.example.schoolregistrationsystem.requestBodyModel.StudentRequestBodyModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class StudentEntityService {
    private final StudentEntityRepository studentEntityRepository;

    public StudentEntity createStudentEntity(StudentRequestBodyModel studentRequestBodyModel) {
        if (studentEntityRepository.findByStudentCardID(studentRequestBodyModel.getStudentCardID()).isPresent()) {
            throw new StudentCardIDAlreadyExistsException();
        }
        return studentEntityRepository.saveAndFlush(new StudentEntity(studentRequestBodyModel));
    }

    public StudentEntity getStudentEntityById(String id) {
        return studentEntityRepository.findById(id).orElseThrow((() -> new ValueNotFoundException(id)));
    }

    public StudentEntity getStudentEntityByStudentCardID(String studentCardID) {
        return studentEntityRepository.findByStudentCardID(studentCardID).orElseThrow((() -> new ValueNotFoundException(studentCardID)));
    }

    public Set<StudentEntity> getAllStudentEntities() {
        return new HashSet<>(studentEntityRepository.findAll());
    }

    public void deleteStudentEntityById(String id) {
        StudentEntity studentEntity = getStudentEntityById(id);
        studentEntityRepository.delete(studentEntity);
    }

    public StudentEntity putStudentEntity(String id, StudentRequestBodyModel studentRequestBodyModel) {
        StudentEntity studentEntity = getStudentEntityById(id);
        BeanUtils.copyProperties(studentRequestBodyModel, studentEntity);
        studentEntityRepository.saveAndFlush(studentEntity);
        return studentEntity;
    }

    public StudentEntity patchStudentEntity(String id, Map<Object, Object> fields) {
        StudentEntity studentEntity = getStudentEntityById(id);
        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(StudentEntity.class, (String) key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, studentEntity, value);
            }
        });
        studentEntityRepository.saveAndFlush(studentEntity);
        return studentEntity;
    }
}
