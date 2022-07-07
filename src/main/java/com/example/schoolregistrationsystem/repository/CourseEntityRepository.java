package com.example.schoolregistrationsystem.repository;

import com.example.schoolregistrationsystem.entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseEntityRepository extends JpaRepository<CourseEntity, String> {

    Optional<CourseEntity> findByNameAndSchoolName(String courseName, String schoolName);
}
