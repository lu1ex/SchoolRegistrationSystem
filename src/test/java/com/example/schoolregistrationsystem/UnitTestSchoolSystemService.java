package com.example.schoolregistrationsystem;


import com.example.schoolregistrationsystem.entity.CourseEntity;
import com.example.schoolregistrationsystem.entity.StudentEntity;
import com.example.schoolregistrationsystem.exceptions.*;
import com.example.schoolregistrationsystem.repository.CourseEntityRepository;
import com.example.schoolregistrationsystem.repository.StudentEntityRepository;
import com.example.schoolregistrationsystem.requestBodyModel.CourseRequestBodyModel;
import com.example.schoolregistrationsystem.requestBodyModel.StudentRequestBodyModel;
import com.example.schoolregistrationsystem.service.CourseEntityService;
import com.example.schoolregistrationsystem.service.SchoolSystemService;
import com.example.schoolregistrationsystem.service.StudentEntityService;
import liquibase.pro.packaged.A;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.*;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class UnitTestSchoolSystemService {
    @Autowired
    private SchoolSystemService schoolSystemService;

    @Autowired
    private CourseEntityService courseEntityService;

    @Autowired
    private StudentEntityService studentEntityService;

    @MockBean
    private StudentEntityRepository studentEntityRepository;

    @MockBean
    private CourseEntityRepository courseEntityRepository;

    @Mock
    StudentEntity studentEntity;

    @Mock
    CourseEntity courseEntity;

    @Test
    public void registerStudentToCourseWorksUnitTest() {
        //given
        String studentCardID = "100100";
        String courseName = "MATH";
        String schoolName = "SCHOOLNR1";
        String expectedResult = "Student registered to course";
        StudentEntity studentEntity = new StudentEntity("ID", "Name", "Surname",
                LocalDate.of(2005, 1, 1), "123456789", "100100", Set.of());
        CourseEntity courseEntity = new CourseEntity("ID", "MATH", "SCHOOLNR1", Set.of());
        int studentsNumberOfCoursesBefore = studentEntity.getCourses().size();

        //when
        Mockito.when(studentEntityRepository.findByStudentCardID(Mockito.anyString())).thenReturn(
                Optional.of(studentEntity));
        Mockito.when(courseEntityRepository.findByNameAndSchoolName(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.of(courseEntity));

        String result = schoolSystemService.registerStudentToCourse(studentCardID, courseName, schoolName);
        int studentsNumberOfCoursesAfter = studentEntity.getCourses().size();

        //then
        assertEquals(result, expectedResult);
        assertEquals(studentsNumberOfCoursesBefore + 1, studentsNumberOfCoursesAfter);
    }

    @Test(expected = StudentReachedMaxNumberOfCourserException.class)
    public void registerStudentToCourseWorksWhenStudentReachedMaxNumberOfCoursesUnitTest() {
        //given
        String studentCardID = "100100";
        String courseName = "MATH";
        String schoolName = "SCHOOLNR1";

        //when
        Mockito.when(studentEntityRepository.findByStudentCardID(Mockito.anyString())).thenReturn(
                Optional.of(studentEntity));
        Mockito.when(courseEntityRepository.findByNameAndSchoolName(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.of(courseEntity));
        Mockito.when(studentEntity.getCourses()).thenReturn(Set.of(new CourseEntity(), new CourseEntity(), new CourseEntity(),
                new CourseEntity(), new CourseEntity()));
        String result = schoolSystemService.registerStudentToCourse(studentCardID, courseName, schoolName);

        //then
        assertEquals(studentEntity.getCourses().size(), 5);
        //Throws StudentReachedMaxNumberOfCourserException student already enrolled to 5 courses (limit is 5)
    }

    @Test(expected = CourseReachedMaxNumberOfStudentsException.class)
    public void registerStudentToCourseWorksWhenCourseReachedMaxNumberOfStudentsUnitTest() {
        //given
        String studentCardID = "100100";
        String courseName = "MATH";
        String schoolName = "SCHOOLNR1";

        //when
        Mockito.when(studentEntityRepository.findByStudentCardID(Mockito.anyString())).thenReturn(
                Optional.of(studentEntity));
        Mockito.when(courseEntityRepository.findByNameAndSchoolName(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.of(courseEntity));
        Mockito.when(courseEntity.getStudents()).thenReturn(Set.of(new StudentEntity(), new StudentEntity(),
                new StudentEntity(), new StudentEntity(), new StudentEntity(), new StudentEntity(),
                new StudentEntity(), new StudentEntity(), new StudentEntity(), new StudentEntity(),
                new StudentEntity(), new StudentEntity(), new StudentEntity(), new StudentEntity(),
                new StudentEntity(), new StudentEntity(), new StudentEntity(), new StudentEntity(),
                new StudentEntity(), new StudentEntity(), new StudentEntity(), new StudentEntity(),
                new StudentEntity(), new StudentEntity(), new StudentEntity(), new StudentEntity(),
                new StudentEntity(), new StudentEntity(), new StudentEntity(), new StudentEntity(),
                new StudentEntity(), new StudentEntity(), new StudentEntity(), new StudentEntity(),
                new StudentEntity(), new StudentEntity(), new StudentEntity(), new StudentEntity(),
                new StudentEntity(), new StudentEntity(), new StudentEntity(), new StudentEntity(),
                new StudentEntity(), new StudentEntity(), new StudentEntity(), new StudentEntity(),
                new StudentEntity(), new StudentEntity(), new StudentEntity(), new StudentEntity()));

        String result = schoolSystemService.registerStudentToCourse(studentCardID, courseName, schoolName);

        //then
        assertEquals(courseEntity.getStudents().size(), 50);
        //Throws StudentReachedMaxNumberOfCourserException course has reached maximum number of student (limit is 50)
    }

    @Test(expected = StudentAlreadyAssignedToCourseException.class)
    public void registerStudentToCourseWorksWhenStudentAlreadyEnrolledToCoursesUnitTest() {
        //given
        String studentCardID = "100100";
        String courseName = "MATH";
        String schoolName = "SCHOOLNR1";

        //when
        Mockito.when(studentEntityRepository.findByStudentCardID(Mockito.anyString())).thenReturn(
                Optional.of(studentEntity));
        Mockito.when(courseEntityRepository.findByNameAndSchoolName(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.of(courseEntity));
        Mockito.when(studentEntity.getCourses()).thenReturn(Set.of(courseEntity));
        String result = schoolSystemService.registerStudentToCourse(studentCardID, courseName, schoolName);

        //then
        assertEquals(studentEntity.getCourses().size(), 5);
        //Throws StudentAlreadyAssignedToCourseException student already enrolled to course
    }

    @Test(expected = ValueNotFoundException.class)
    public void registerStudentToCourseWorksWhenStudentNotExistsInDBUnitTest() {
        //given
        String studentCardID = "999999";
        String courseName = "MATH";
        String schoolName = "SCHOOLNR1";

        //when
        Mockito.when(studentEntityRepository.findByStudentCardID(Mockito.anyString())).thenReturn(
                Optional.empty());
        Mockito.when(courseEntityRepository.findByNameAndSchoolName(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.of(courseEntity));
        String result = schoolSystemService.registerStudentToCourse(studentCardID, courseName, schoolName);

        //then
        //Throws ValueNotFoundException student not exist in DataBase
    }

    @Test(expected = ValueNotFoundException.class)
    public void registerStudentToCourseWorksWhenCourseNotExistsInDBUnitTest() {
        //given
        String studentCardID = "100100";
        String courseName = "MATH";
        String schoolName = "SCHOOLNR999999";

        //when
        Mockito.when(studentEntityRepository.findByStudentCardID(Mockito.anyString())).thenReturn(
                Optional.of(studentEntity));
        Mockito.when(courseEntityRepository.findByNameAndSchoolName(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.empty());
        String result = schoolSystemService.registerStudentToCourse(studentCardID, courseName, schoolName);

        //then
        //Throws ValueNotFoundException course not exist in DataBase
    }

    @Test
    public void unregisterStudentFromCourseWorkUnitTest() {
        //given
        String studentCardID = "100100";
        String courseName = "MATH";
        String schoolName = "SCHOOLNR1";
        String expectedResponse = "Student unregistered from course";

        //when
        Mockito.when(studentEntityRepository.findByStudentCardID(Mockito.anyString())).thenReturn(
                Optional.of(studentEntity));
        Mockito.when(courseEntityRepository.findByNameAndSchoolName(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.of(courseEntity));
        Mockito.when(studentEntity.getCourses()).thenReturn(Set.of(courseEntity));
        String result = schoolSystemService.unregisterStudentFromCourse(studentCardID, courseName, schoolName);

        //then
        assertEquals(expectedResponse, result);
    }

    @Test(expected = StudentNotAssignedToCourseException.class)
    public void unregisterStudentFromCourseWorksWhenStudentWasNotEnrolledToCourseUnitTest() {
        //given
        String studentCardID = "100100";
        String courseName = "MATH";
        String schoolName = "SCHOOLNR1";

        //when
        Mockito.when(studentEntityRepository.findByStudentCardID(Mockito.anyString())).thenReturn(
                Optional.of(studentEntity));
        Mockito.when(courseEntityRepository.findByNameAndSchoolName(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.of(courseEntity));
        Mockito.when(studentEntity.getCourses()).thenReturn(Set.of());
        String result = schoolSystemService.unregisterStudentFromCourse(studentCardID, courseName, schoolName);

        //then
        //Throws StudentNotAssignedToCourseException because student was to enrolled to course
    }

    @Test(expected = ValueNotFoundException.class)
    public void unregisterStudentFromCourseWorksWhenStudentNotExistsInDBUnitTest() {
        //given
        String studentCardID = "999999";
        String courseName = "MATH";
        String schoolName = "SCHOOLNR1";

        //when
        Mockito.when(studentEntityRepository.findByStudentCardID(Mockito.anyString())).thenReturn(
                Optional.empty());
        Mockito.when(courseEntityRepository.findByNameAndSchoolName(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.of(courseEntity));
        String result = schoolSystemService.unregisterStudentFromCourse(studentCardID, courseName, schoolName);

        //then
        //Throws ValueNotFoundException student not exist in DataBase
    }

    @Test(expected = ValueNotFoundException.class)
    public void unregisterStudentFromCourseWorksWhenCourseNotExistsInDBUnitTest() {
        //given
        String studentCardID = "100100";
        String courseName = "MATH";
        String schoolName = "SCHOOLNR999999";

        //when
        Mockito.when(studentEntityRepository.findByStudentCardID(Mockito.anyString())).thenReturn(
                Optional.of(studentEntity));
        Mockito.when(courseEntityRepository.findByNameAndSchoolName(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.empty());
        String result = schoolSystemService.unregisterStudentFromCourse(studentCardID, courseName, schoolName);

        //then
        //Throws ValueNotFoundException course not exist in DataBase
    }

    @Test
    public void getAllStudentsFromCourseWorksUnitTest() {
        //given
        String courseName = "MATH";
        String schoolName = "SCHOOLNR1";
        Set<StudentEntity> expectedResult = Set.of(new StudentEntity("ID1", "Name1", "Surname1",
                LocalDate.of(2005, 1, 1), "123456789", "100101",
                Set.of()), new StudentEntity("ID2", "Name2", "Surname2",
                LocalDate.of(2005, 1, 1), "123456789", "100102", Set.of()));

        //when
        Mockito.when(courseEntityRepository.findByNameAndSchoolName(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.of(courseEntity));
        Mockito.when(courseEntity.getStudents()).thenReturn(Set.of(new StudentEntity("ID1", "Name1", "Surname1",
                        LocalDate.of(2005, 1, 1), "123456789", "100101", Set.of()),
                new StudentEntity("ID2", "Name2", "Surname2",
                        LocalDate.of(2005, 1, 1), "123456789", "100102", Set.of())));
        Set<StudentEntity> result = schoolSystemService.getAllStudentsFromCourse(courseName, schoolName);

        //then
        assertEquals(result.toString(), expectedResult.toString());
    }

    @Test(expected = ValueNotFoundException.class)
    public void getAllStudentsFromCourseWorksWhenCourseNotExistUnitTest() {
        //given
        String courseName = "MATH";
        String schoolName = "SCHOOLNR999999";

        //when
         Mockito.when(courseEntityRepository.findByNameAndSchoolName(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.empty());
        Set<StudentEntity> result = schoolSystemService.getAllStudentsFromCourse(courseName, schoolName);

        //then
        //Throws ValueNotFoundException course not exist in DataBase
    }

    @Test
    public void getAllCoursesFromStudentWorksUnitTest() {
        //given
        String studentCardID= "100100";
        Set<CourseEntity> expectedResult = Set.of(new CourseEntity("ID1", "NAME1", "SCHOOLNAME1", Set.of()),
                new CourseEntity("ID2", "NAME2", "SCHOOLNAME2", Set.of()));

        //when
        Mockito.when(studentEntityRepository.findByStudentCardID(Mockito.anyString())).thenReturn(
                Optional.of(studentEntity));
        Mockito.when(studentEntity.getCourses()).thenReturn(Set.of(new CourseEntity("ID1", "NAME1",
                        "SCHOOLNAME1", Set.of()), new CourseEntity("ID2", "NAME2",
                "SCHOOLNAME2", Set.of())));
        Set<CourseEntity> result = schoolSystemService.getAllCoursesFromStudent(studentCardID);

        //then
        assertEquals(result.toString(), expectedResult.toString());
    }

    @Test(expected = ValueNotFoundException.class)
    public void getAllCoursesFromStudentWorksWhenStudentNotExistUnitTest() {
        //given
        String studentCardID= "999999";

        //when
        Mockito.when(studentEntityRepository.findByStudentCardID(Mockito.anyString())).thenReturn(Optional.empty());
        Set<CourseEntity> result = schoolSystemService.getAllCoursesFromStudent(studentCardID);

        //then
        //Throws ValueNotFoundException student not exist in DataBase
    }

    @Test
    public void getAllStudentsWithoutEnrolledCoursesWorksUnitTest() {
        //given
        Set<StudentEntity> expectedResult = Set.of(new StudentEntity("ID1", "Name1", "Surname1",
                LocalDate.of(2005, 1, 1), "123456789", "100101",
                Set.of()), new StudentEntity("ID3", "Name3", "Surname3",
                LocalDate.of(2015, 1, 1), "123456787", "100103", Set.of()));

        //when
        Mockito.when(studentEntityRepository.findAll()).thenReturn(
                List.of(new StudentEntity("ID1", "Name1", "Surname1",
                        LocalDate.of(2005, 1, 1), "123456789", "100101",
                        Set.of()),
                        new StudentEntity("ID2", "Name2", "Surname2",
                        LocalDate.of(2010, 1, 1), "123456788", "100102",
                                Set.of(new CourseEntity(), new CourseEntity())),
                        new StudentEntity("ID3", "Name3", "Surname3",
                                LocalDate.of(2015, 1, 1), "123456787", "100103",
                                Set.of()),
                        new StudentEntity("ID4", "Name4", "Surname4",
                                LocalDate.of(2009, 1, 1), "123456786", "100104",
                                Set.of(new CourseEntity()))));
         Set<StudentEntity> result = schoolSystemService.getAllStudentsWithoutEnrolledCourses();

        //then
        assertEquals(result.toString(), expectedResult.toString());
    }

    @Test
    public void getAllCoursesWithoutEnrolledStudentsWorksUnitTest() {
        //given
        String schoolName = "SCHOOLNR1";
        Set<CourseEntity> expectedResult = Set.of(new CourseEntity("ID1", "NAME1", "SCHOOLNR1",
                        Set.of()),
                new CourseEntity("ID3", "NAME3", "SCHOOLNR1", Set.of()));

        //when
        Mockito.when(courseEntityRepository.findAllBySchoolName(Mockito.anyString())).thenReturn(
                Set.of(new CourseEntity("ID1", "NAME1", "SCHOOLNR1", Set.of()),
                        new CourseEntity("ID2", "NAME2", "SCHOOLNR1",
                                Set.of(new StudentEntity(), new StudentEntity(), new StudentEntity())),
                        new CourseEntity("ID3", "NAME3", "SCHOOLNR1", Set.of()),
                        new CourseEntity("ID4", "NAME4", "SCHOOLNR1",
                                Set.of(new StudentEntity(), new StudentEntity()))));
        Set<CourseEntity> result = schoolSystemService.getAllCoursesWithoutEnrolledStudents(schoolName);

        //then
        assertEquals(result.toString(), expectedResult.toString());
    }
}
