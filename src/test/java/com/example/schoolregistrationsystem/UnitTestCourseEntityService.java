package com.example.schoolregistrationsystem;

import com.example.schoolregistrationsystem.entity.CourseEntity;
import com.example.schoolregistrationsystem.exceptions.CourseAlreadyExistsException;
import com.example.schoolregistrationsystem.exceptions.ValueNotFoundException;
import com.example.schoolregistrationsystem.repository.CourseEntityRepository;
import com.example.schoolregistrationsystem.requestBodyModel.CourseRequestBodyModel;
import com.example.schoolregistrationsystem.service.CourseEntityService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UnitTestCourseEntityService {

    @Autowired
    private CourseEntityService courseEntityService;

    @MockBean
    private CourseEntityRepository courseEntityRepository;

    @Test
    public void createCourseEntityWorksUnitTest() {
        //given
        CourseRequestBodyModel courseRequestBodyModel = new CourseRequestBodyModel("Name", "SchoolName");
        CourseEntity expectedResult = new CourseEntity("ID", "NAME", "SCHOOLNAME", Set.of());

        //when
        Mockito.when(courseEntityRepository.findByNameAndSchoolName(Mockito.any(), Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(courseEntityRepository.saveAndFlush(Mockito.any())).thenReturn(
                new CourseEntity("ID", "NAME", "SCHOOLNAME", Set.of()));
        CourseEntity result = courseEntityService.createCourseEntity(courseRequestBodyModel);

        //then
        assertEquals(result.toString(), expectedResult.toString());
    }

    @Test(expected = CourseAlreadyExistsException.class)
    public void createCourseEntityThrowExceptionWhenCourseAlreadyExistsTest() {
        //given
        CourseRequestBodyModel courseRequestBodyModel = new CourseRequestBodyModel("Name", "SchoolName");
        CourseEntity expectedResult = new CourseEntity("ID", "NAME", "SCHOOLNAME", Set.of());

         //when
        Mockito.when(courseEntityRepository.findByNameAndSchoolName(Mockito.anyString(), Mockito.anyString())).thenReturn(
                Optional.of(new CourseEntity("ID", "NAME", "SCHOOLNAME", Set.of())));
        CourseEntity result = courseEntityService.createCourseEntity(courseRequestBodyModel);

        //given
        //Throws CourseAlreadyExistsException because there is already course with that name in school
    }

    @Test
    public void getCourseEntityByIdWorksUnitTest() {
        //given
        String courseID = "000000000000000000000000000000id";
        CourseEntity expectedCourseEntity = new CourseEntity("000000000000000000000000000000id",
                "name", "schoolName", Set.of());

        //when
        Mockito.when(courseEntityRepository.findById(Mockito.anyString())).thenReturn(
                Optional.of(new CourseEntity("000000000000000000000000000000id", "name", "schoolName",
                        Set.of())));
        CourseEntity result = courseEntityService.getCourseEntityById(courseID);

        //then
        assertEquals(result.getId(), expectedCourseEntity.getId());
        assertEquals(result.getName(), expectedCourseEntity.getName());
        assertEquals(result.getSchoolName(), expectedCourseEntity.getSchoolName());
        assertEquals(result.getStudents(), expectedCourseEntity.getStudents());
    }

    @Test(expected = ValueNotFoundException.class)
    public void getCourseEntityByIdThrowExceptionWhenIDNotExistsUnitTest() {
        //given
        String courseID = "000000000000000000000idNotExists";

        //when
        Mockito.when(courseEntityRepository.findById(Mockito.anyString())).thenThrow(ValueNotFoundException.class);
        CourseEntity result = courseEntityService.getCourseEntityById(courseID);

        //given
        //Throws ValueNotFoundException because repository does not find course by id
    }

    @Test
    public void getCourseEntityByNameAndSchoolNameWorksUnitTest() {
        //given
        String courseName = "NAME";
        String schoolName = "SCHOOL";
        CourseEntity expectedCourseEntity = new CourseEntity("000000000000000000000000000000id",
                "NAME", "SCHOOL", Set.of());

        //when
        Mockito.when(courseEntityRepository.findByNameAndSchoolName(Mockito.anyString(),
                Mockito.anyString())).thenReturn(Optional.of(new CourseEntity("000000000000000000000000000000id",
                "NAME", "SCHOOL", Set.of())));
        CourseEntity result = courseEntityService.getCourseEntityByNameAndSchoolName(courseName, schoolName);

        //then
        assertEquals(result.getId(), expectedCourseEntity.getId());
        assertEquals(result.getName(), expectedCourseEntity.getName());
        assertEquals(result.getSchoolName(), expectedCourseEntity.getSchoolName());
        assertEquals(result.getStudents(), expectedCourseEntity.getStudents());
    }

    @Test(expected = ValueNotFoundException.class)
    public void getCourseEntityByNameAndSchoolNameThrowExceptionWhenCourseNotExistsUnitTest() {
        //given
        String courseName = "NameWhichNotExist";
        String schoolName = "SchoolWhichNotExist";

        //when
        Mockito.when(courseEntityRepository.findByNameAndSchoolName(Mockito.anyString(), Mockito.anyString()))
                .thenThrow(ValueNotFoundException.class);
        CourseEntity result = courseEntityService.getCourseEntityByNameAndSchoolName(courseName, schoolName);

        //given
        //Throws ValueNotFoundException because repository does not find course by name and schoolName
    }

    @Test
    public void getCourseEntityByNameWorksUnitTest() {
        //given
        String courseName = "NAME";
        CourseEntity expectedCourseEntity = new CourseEntity("000000000000000000000000000000id",
                "NAME", "SCHOOL", Set.of());

        //when
        Mockito.when(courseEntityRepository.findByName(Mockito.anyString())).thenReturn(
                Optional.of(new CourseEntity("000000000000000000000000000000id",
                "NAME", "SCHOOL", Set.of())));
        CourseEntity result = courseEntityService.getCourseEntityByName(courseName);

        //then
        assertEquals(result.getId(), expectedCourseEntity.getId());
        assertEquals(result.getName(), expectedCourseEntity.getName());
        assertEquals(result.getSchoolName(), expectedCourseEntity.getSchoolName());
        assertEquals(result.getStudents(), expectedCourseEntity.getStudents());
    }

    @Test(expected = ValueNotFoundException.class)
    public void getCourseEntityByNameThrowExceptionWhenCourseNotExistsUnitTest() {
        //given
        String courseName = "NameWhichNotExist";

        //when
        Mockito.when(courseEntityRepository.findByName(Mockito.anyString()))
                .thenThrow(ValueNotFoundException.class);
        CourseEntity result = courseEntityService.getCourseEntityByName(courseName);

        //given
        //Throws ValueNotFoundException because repository does not find course by name
    }

    @Test
    public void getAllCoursesEntitiesWorksUnitTest() {
        //given
        Set<CourseEntity> expectedSetOfEntity = Set.of(
                new CourseEntity("000000000000000000000000000000id", "NAME", "SCHOOL", Set.of()),
                new CourseEntity("000000000000000000000000000002id", "NAME2", "SCHOOL2", Set.of()));

        //when
        Mockito.when(courseEntityRepository.findAll()).thenReturn(
                List.of(new CourseEntity("000000000000000000000000000000id",
                        "NAME", "SCHOOL", Set.of()), new CourseEntity("000000000000000000000000000002id",
                        "NAME2", "SCHOOL2", Set.of())));
        Set<CourseEntity> result = courseEntityService.getAllCoursesEntities();

        //then
        assertEquals(result.toString(), expectedSetOfEntity.toString());
    }

    @Test
    public void getAllCourseEntitiesBySchoolNameWorksUnitTest() {
        //given
        String schoolName = "SCHOOLNAME";
        Set<CourseEntity> expectedSetOfEntity = Set.of(
                new CourseEntity("000000000000000000000000000000id", "NAME", "SCHOOL", Set.of()),
                new CourseEntity("000000000000000000000000000002id", "NAME2", "SCHOOL2", Set.of()));

        //when
        Mockito.when(courseEntityRepository.findAllBySchoolName(Mockito.anyString())).thenReturn(
                Set.of(new CourseEntity("000000000000000000000000000000id", "NAME", "SCHOOL", Set.of()),
                        new CourseEntity("000000000000000000000000000002id", "NAME2", "SCHOOL2", Set.of())));
        Set<CourseEntity> result = courseEntityService.getAllCourseEntitiesBySchoolName(schoolName);

        //then
        assertEquals(result.toString(), expectedSetOfEntity.toString());
    }

    @Test
    public void deleteCourseEntityByIdWorksUnitTest() {
        //given
        String courseID = "000000000000000000000000000000id";
        Optional<CourseEntity> optionalCourseEntity = Optional.of(new CourseEntity("000000000000000000000000000000id",
                "name", "schoolName", Set.of()));
        //when
        Mockito.when(courseEntityRepository.findById(courseID)).thenReturn(Optional.of(new CourseEntity(
                "000000000000000000000000000000id", "name", "schoolName", Set.of())));
        courseEntityService.deleteCourseEntityById(courseID);

        //then
    }

    @Test(expected = ValueNotFoundException.class)
    public void deleteCourseEntityByIdThrowExceptionWhenIDNotExistsUnitTest() {
        //given
        String courseID = "000000000000000000000idNotExists";

        //when
        Mockito.when(courseEntityService.getCourseEntityById(Mockito.anyString())).thenThrow(ValueNotFoundException.class);
        courseEntityService.deleteCourseEntityById(courseID);

        //given
        //Throws ValueNotFoundException because repository does not find course by id
    }

    @Test
    public void putCourseEntityWorksUnitTest() {
        //given
        String courseID = "ID";
        CourseRequestBodyModel courseRequestBodyModel = new CourseRequestBodyModel(
                "ModifiedName", "ModifiedSchoolName");
        CourseEntity courseEntityBefore = new CourseEntity("ID", "NAME", "SCHOOLNAME", Set.of());

        //when
        Mockito.when(courseEntityRepository.findById(Mockito.anyString())).thenReturn(Optional.of(new CourseEntity(
                "ID", "NAME", "SCHOOLNAME", Set.of())));
        CourseEntity result = courseEntityService.putCourseEntity(courseID, courseRequestBodyModel);

        //then
        assertNotEquals(result.getName(), courseEntityBefore.getName());
        assertNotEquals(result.getSchoolName(), courseEntityBefore.getSchoolName());
        assertEquals(result.getName(), courseRequestBodyModel.getName());
        assertEquals(result.getSchoolName(), courseRequestBodyModel.getSchoolName());
    }

    @Test(expected = ValueNotFoundException.class)
    public void putCourseEntityThrowExceptionWhenCourseNotExistsTest() {
        //given
        String courseID = "ID";
        CourseRequestBodyModel courseRequestBodyModel = new CourseRequestBodyModel(
                "ModifiedName", "ModifiedSchoolName");
        CourseEntity courseEntityBefore = new CourseEntity("ID", "NAME", "SCHOOLNAME", Set.of());

        //when
        Mockito.when(courseEntityRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
        CourseEntity result = courseEntityService.putCourseEntity(courseID, courseRequestBodyModel);

        //given
        //Throws ValueNotFoundException because repository does not find course by ID
    }

    @Test
    public void patchCourseEntityWorksUnitTest() {
        //given
        String courseID = "ID";
        Map<Object, Object> fieldsToChange = Map.of("name", "ModifiedName");
        CourseEntity courseEntityBefore = new CourseEntity("ID", "NAME", "SCHOOLNAME", Set.of());

        //when
        Mockito.when(courseEntityRepository.findById(Mockito.anyString())).thenReturn(Optional.of(new CourseEntity(
                "ID", "NAME", "SCHOOLNAME", Set.of())));
        CourseEntity result = courseEntityService.patchCourseEntity(courseID, fieldsToChange);

        //then
        assertNotEquals(result.getName(), courseEntityBefore.getName());
        assertEquals(result.getName(), fieldsToChange.get("name"));
        assertEquals(result.getSchoolName(), courseEntityBefore.getSchoolName());
    }

    @Test(expected = ValueNotFoundException.class)
    public void patchCourseEntityThrowExceptionWhenCourseNotExistsTest() {
        //given
        String courseID = "ID";
        Map<Object, Object> fieldsToChange = Map.of("name", "ModifiedName");

        //when
        Mockito.when(courseEntityRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
        CourseEntity result = courseEntityService.patchCourseEntity(courseID, fieldsToChange);

        //given
        //Throws ValueNotFoundException because repository does not find course by ID
    }
}
