package com.example.schoolregistrationsystem.repository;

import com.example.schoolregistrationsystem.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import java.util.Optional;

public interface StudentEntityRepository extends JpaRepository<StudentEntity, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Transactional
    Optional<StudentEntity> findByStudentCardID(String studentCardID);
}
