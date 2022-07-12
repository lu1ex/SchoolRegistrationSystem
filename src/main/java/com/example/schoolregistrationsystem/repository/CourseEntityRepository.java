package com.example.schoolregistrationsystem.repository;

import com.example.schoolregistrationsystem.entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;

public interface CourseEntityRepository extends JpaRepository<CourseEntity, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Transactional
    Optional<CourseEntity> findByName(String courseName);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Transactional
    Optional<CourseEntity> findByNameAndSchoolName(String courseName, String schoolName);

    Set<CourseEntity> findAllBySchoolName(String schoolName);
}
