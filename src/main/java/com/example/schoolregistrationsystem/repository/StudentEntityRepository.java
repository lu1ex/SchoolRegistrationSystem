package com.example.schoolregistrationsystem.repository;

import com.example.schoolregistrationsystem.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentEntityRepository extends JpaRepository<StudentEntity, String> {

    Optional<StudentEntity> findByStudentCardID(String studentCardID);
}
