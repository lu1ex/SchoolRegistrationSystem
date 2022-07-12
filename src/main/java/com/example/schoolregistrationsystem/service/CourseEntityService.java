package com.example.schoolregistrationsystem.service;

import com.example.schoolregistrationsystem.entity.CourseEntity;
import com.example.schoolregistrationsystem.exceptions.CourseAlreadyExistsException;
import com.example.schoolregistrationsystem.exceptions.ValueNotFoundException;
import com.example.schoolregistrationsystem.repository.CourseEntityRepository;
import com.example.schoolregistrationsystem.requestBodyModel.CourseRequestBodyModel;
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
public class CourseEntityService {
    private final CourseEntityRepository courseEntityRepository;

    public CourseEntity createCourseEntity(CourseRequestBodyModel courseRequestBodyModel) {
        if (courseEntityRepository.findByNameAndSchoolName(courseRequestBodyModel.getName().toUpperCase(),
                courseRequestBodyModel.getSchoolName().toUpperCase()).isPresent()) {
            throw new CourseAlreadyExistsException();
        }
        CourseEntity newCourseEntity = new CourseEntity(courseRequestBodyModel);
        courseEntityRepository.saveAndFlush(newCourseEntity);
        return newCourseEntity;
    }

    public CourseEntity getCourseEntityById(String id) {
        return courseEntityRepository.findById(id).orElseThrow((() -> new ValueNotFoundException(id)));
    }

    public CourseEntity getCourseEntityByNameAndSchoolName(String courseName, String schoolName) {
        return courseEntityRepository.findByNameAndSchoolName(courseName.toUpperCase(), schoolName)
                .orElseThrow((() -> new ValueNotFoundException(courseName)));
    }

    public CourseEntity getCourseEntityByName(String courseName) {
        return courseEntityRepository.findByName(courseName.toUpperCase()).orElseThrow((() -> new ValueNotFoundException(courseName)));
    }

    public Set<CourseEntity> getAllCoursesEntities() {
        return new HashSet<>(courseEntityRepository.findAll());
    }

    public Set<CourseEntity> getAllCourseEntitiesBySchoolName(String schoolName) {
        return new HashSet<>(courseEntityRepository.findAllBySchoolName(schoolName));
    }

    public void deleteCourseEntityById(String id) {
        courseEntityRepository.deleteById(id);
    }

    public CourseEntity putCourseEntity(String id, CourseRequestBodyModel courseRequestBodyModel) {
        CourseEntity courseEntity = getCourseEntityById(id);
        BeanUtils.copyProperties(courseRequestBodyModel, courseEntity);
        courseEntityRepository.saveAndFlush(courseEntity);
        return courseEntity;
    }

    public CourseEntity patchCourseEntity(String id, Map<Object, Object> fields) {
        CourseEntity courseEntity = getCourseEntityById(id);
        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(CourseEntity.class, (String) key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, courseEntity, value);
            }
        });
        courseEntityRepository.saveAndFlush(courseEntity);
        return courseEntity;
    }
}
