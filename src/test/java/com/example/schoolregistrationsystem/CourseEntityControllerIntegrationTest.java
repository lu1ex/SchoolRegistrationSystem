package com.example.schoolregistrationsystem;


import com.example.schoolregistrationsystem.DTO.CourseFullDTO;
import com.example.schoolregistrationsystem.entity.CourseEntity;
import com.example.schoolregistrationsystem.exceptions.ValueNotFoundException;
import com.example.schoolregistrationsystem.repository.CourseEntityRepository;
import com.example.schoolregistrationsystem.requestBodyModel.CourseRequestBodyModel;
import com.example.schoolregistrationsystem.service.CourseEntityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class CourseEntityControllerIntegrationTest {

    @Inject
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CourseEntityRepository courseEntityRepository;

    @Autowired
    private CourseEntityService courseEntityService;

    @Test
    @Sql(scripts = {"classpath:db_test/student_and_course_crud_db_test.sql"})
    void createCourseEntityIntegrationTestWorks() throws Exception {
        //given
        CourseRequestBodyModel courseToCreateBodyModel = new CourseRequestBodyModel("CHEMISTRY", "SCHOOL1");
        CourseFullDTO expectedResponseObject = new CourseFullDTO("CHEMISTRY", "SCHOOL1", new HashSet<>());
        int numberOfCoursesInDBBefore = courseEntityRepository.findAll().size();

        //when
        MvcResult result = mvc.perform(post("/course")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(courseToCreateBodyModel)))
                .andDo(print())
                .andReturn();

        int numberOfCoursesInDBAfter = courseEntityRepository.findAll().size();

        //then
        assertEquals(numberOfCoursesInDBAfter, (numberOfCoursesInDBBefore + 1));
        assertTrue(result.getResponse().getContentAsString().contains(objectMapper.writeValueAsString(expectedResponseObject)));
        assertEquals(201, result.getResponse().getStatus());
    }

    @Test //throws CourseAlreadyExistsException
    @Sql(scripts = {"classpath:db_test/student_and_course_crud_db_test.sql"})
    void createCourseEntityIntegrationTestThrowsBadRequestWhenCourseAlreadyExistsInDB() throws Exception {
        //given
        CourseRequestBodyModel courseToCreateBodyModel = new CourseRequestBodyModel("HISTORY", "SCHOOLNR1");
        int numberOfCoursesInDBBefore = courseEntityRepository.findAll().size();

        //when
        MvcResult result = mvc.perform(post("/course")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(courseToCreateBodyModel)))
                .andDo(print())
                .andReturn();

        int numberOfCoursesInDBAfter = courseEntityRepository.findAll().size();

        //then
        assertEquals(numberOfCoursesInDBAfter, numberOfCoursesInDBBefore);
        assertTrue(result.getResponse().getContentAsString()
                .contains("There is already courser with that name in that school."));
        assertEquals(400, result.getResponse().getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:db_test/student_and_course_crud_db_test.sql"})
    void getCourseEntityByIdWorks() throws Exception {
        //given
        String courseID = "00000000000000000000000000000111";
        CourseFullDTO expectedResponseObject = new CourseFullDTO("MATH", "SCHOOLNR1", Set.of());

        //when
        MvcResult result = mvc.perform(get("/course/" + courseID))
                .andDo(print())
                .andReturn();

        //then
        assertTrue(result.getResponse().getContentAsString().contains(objectMapper.writeValueAsString(expectedResponseObject)));
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:db_test/student_and_course_crud_db_test.sql"})
    void getCourseEntityByIdThrowsBadRequestWhenCourseNotExistsInDB() throws Exception {
        //given
        String courseID = "0000000000000000000000idNotExist";

        //when
        MvcResult result = mvc.perform(get("/course/" + courseID))
                .andDo(print())
                .andReturn();

        //then
        assertTrue(result.getResponse().getContentAsString()
                .contains("Object with id 0000000000000000000000idNotExist not found"));
        assertEquals(404, result.getResponse().getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:db_test/student_and_course_crud_db_test.sql"})
    void getCourseEntityByNameAndSchoolNameWorks() {
        //given
        String courseName = "MATH";
        String schoolName = "SCHOOLNR1";
        CourseEntity expectedCourseEntity = new CourseEntity("00000000000000000000000000000111", "MATH",
                "SCHOOLNR1", Set.of());

        //when
        CourseEntity result = courseEntityService.getCourseEntityByNameAndSchoolName(courseName, schoolName);

        //then
        assertEquals(expectedCourseEntity.toString(), result.toString());
    }

    @Test
    @Sql(scripts = {"classpath:db_test/student_and_course_crud_db_test.sql"})
    void getCourseEntityByNameAndSchoolNameThrowsBadRequestWhenCourseNotExistsInDB() {
        //given
        String courseName = "MATH";
        String schoolName = "SCHOOLNR999";

        //then
        assertThrows(ValueNotFoundException.class,
                () -> courseEntityService.getCourseEntityByNameAndSchoolName(courseName, schoolName));
    }

    @Test
    @Sql(scripts = {"classpath:db_test/student_and_course_crud_db_test.sql"})
    void getAllCoursesEntitiesWorks() throws Exception {
        //given
        Set<CourseFullDTO> expectedResponseObject = Set.of(
                new CourseFullDTO("HISTORY", "SCHOOLNR1", Set.of()),
                new CourseFullDTO("BIOLOGY", "SCHOOLNR1", Set.of()),
                new CourseFullDTO("MATH", "SCHOOLNR1", Set.of())
        );

        //when
        MvcResult result = mvc.perform(get("/course/all"))
                .andDo(print())
                .andReturn();

        //then
        assertTrue(result.getResponse().getContentAsString().contains(objectMapper.writeValueAsString(expectedResponseObject)));
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:db_test/student_and_course_crud_db_test.sql"})
    void deleteCourseEntityByIdWorks() throws Exception {
        //given
        String courseID = "00000000000000000000000000000111";
        int numberOfCoursesInDBBefore = courseEntityRepository.findAll().size();

        //when
        MvcResult result = mvc.perform(delete("/course/" + courseID))
                .andDo(print())
                .andReturn();
        int numberOfCoursesInDBAfter = courseEntityRepository.findAll().size();

        //then
        assertTrue(numberOfCoursesInDBAfter < numberOfCoursesInDBBefore);
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:db_test/student_and_course_crud_db_test.sql"})
    void deleteCourseEntityThrowsExceptionWhenCourseNotExists() throws Exception {
        //given
        String courseID = "000000000000000000000idNotExists";
        int numberOfCoursesInDBBefore = courseEntityRepository.findAll().size();

        //when
        MvcResult result = mvc.perform(delete("/course/" + courseID))
                .andDo(print())
                .andReturn();
        int numberOfCoursesInDBAfter = courseEntityRepository.findAll().size();

        //then
        assertEquals(numberOfCoursesInDBAfter, numberOfCoursesInDBBefore);
        assertTrue(result.getResponse().getContentAsString()
                .contains("Object with id 000000000000000000000idNotExists not found"));
        assertEquals(404, result.getResponse().getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:db_test/student_and_course_crud_db_test.sql"})
    void putCourseEntityWorks() throws Exception {
        //given
        String courseID = "00000000000000000000000000000111";
        CourseRequestBodyModel courseRequestBodyModel = new CourseRequestBodyModel();
        courseRequestBodyModel.setName("PHYSICS");
        courseRequestBodyModel.setSchoolName("SCHOOLNRX"); //was MATH

        CourseFullDTO expectedResponseObject = new CourseFullDTO("PHYSICS", "SCHOOLNRX", Set.of());

        //when
        MvcResult result = mvc.perform(put("/course/" + courseID)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(courseRequestBodyModel)))
                .andDo(print())
                .andReturn();

        //then
        assertTrue(result.getResponse().getContentAsString().contains(objectMapper.writeValueAsString(expectedResponseObject)));
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:db_test/student_and_course_crud_db_test.sql"})
    void putCourseEntityThrowsExceptionWhenCourseNotExists() throws Exception {
        //given
        String courseID = "000000000000000000000idNotExists";
        CourseRequestBodyModel courseRequestBodyModel = new CourseRequestBodyModel();
        courseRequestBodyModel.setName("PHYSICS");
        courseRequestBodyModel.setSchoolName("SCHOOLNRX"); //was MATH

        //when
        MvcResult result = mvc.perform(put("/course/" + courseID)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(courseRequestBodyModel)))
                .andDo(print())
                .andReturn();

        //then
        assertTrue(result.getResponse().getContentAsString()
                .contains("Object with id 000000000000000000000idNotExists not found"));
        assertEquals(404, result.getResponse().getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:db_test/student_and_course_crud_db_test.sql"})
    void patchCourseEntityWorks() throws Exception {
        //given
        String courseID = "00000000000000000000000000000111";
        Map<String, String> fieldToChangeAndNewValue = Map.of("name", "PHYSICS");

        CourseFullDTO expectedResponseObject = new CourseFullDTO("PHYSICS", "SCHOOLNR1", Set.of());

        //when
        MvcResult result = mvc.perform(patch("/course/" + courseID)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(fieldToChangeAndNewValue)))
                .andDo(print())
                .andReturn();

        //then
        assertTrue(result.getResponse().getContentAsString().contains(objectMapper.writeValueAsString(expectedResponseObject)));
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:db_test/student_and_course_crud_db_test.sql"})
    void patchCourseEntityThrowsExceptionWhenCourseNotExists() throws Exception {
        //given
        String courseID = "000000000000000000000idNotExists";
        Map<String, String> fieldToChangeAndNewValue = Map.of("name", "PHYSICS");

        //when
        MvcResult result = mvc.perform(patch("/course/" + courseID)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(fieldToChangeAndNewValue)))
                .andDo(print())
                .andReturn();

        //then
        assertTrue(result.getResponse().getContentAsString()
                .contains("Object with id 000000000000000000000idNotExists not found"));
        assertEquals(404, result.getResponse().getStatus());
    }
}
