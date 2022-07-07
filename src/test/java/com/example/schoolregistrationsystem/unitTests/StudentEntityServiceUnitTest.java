package com.example.schoolregistrationsystem.unitTests;

import com.example.schoolregistrationsystem.entity.StudentEntity;
import com.example.schoolregistrationsystem.repository.StudentEntityRepository;
import com.example.schoolregistrationsystem.service.ModelMapperService;
import com.example.schoolregistrationsystem.service.StudentEntityService;
import org.junit.Before;

public class StudentEntityServiceUnitTest {
    private StudentEntity studentEntity;
    private StudentEntityService studentEntityService;
    private StudentEntityRepository studentEntityRepository;
    private ModelMapperService modelMapperService;

    @Before
    public void setUp() {
        studentEntity = new StudentEntity();
        studentEntityService = new StudentEntityService(studentEntityRepository);
    }


}
