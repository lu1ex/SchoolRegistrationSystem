package com.example.schoolregistrationsystem;

import com.example.schoolregistrationsystem.DTO.CourseDTO;
import com.example.schoolregistrationsystem.DTO.CourseFullDTO;
import com.example.schoolregistrationsystem.DTO.StudentDTO;
import com.example.schoolregistrationsystem.DTO.StudentFullDTO;
import com.example.schoolregistrationsystem.repository.CourseEntityRepository;
import com.example.schoolregistrationsystem.repository.StudentEntityRepository;
import com.example.schoolregistrationsystem.requestBodyModel.RegisterStudentRequestBodyModel;
import com.example.schoolregistrationsystem.service.CourseEntityService;
import com.example.schoolregistrationsystem.service.StudentEntityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.inject.Inject;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class SchoolSystemControllerIntegrationTest {

    @Inject
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CourseEntityRepository courseEntityRepository;

    @Autowired
    private CourseEntityService courseEntityService;

    @Autowired
    private StudentEntityRepository studentEntityRepository;

    @Autowired
    private StudentEntityService studentEntityService;

    @Test
    @Sql(scripts = {"classpath:db_test/school_system_db_test.sql"})
    void registerStudentToCourseWorks() throws Exception {
        //given
        RegisterStudentRequestBodyModel registerStudentRequestBodyModel = new RegisterStudentRequestBodyModel(
                "999998", "BIOLOGY", "SCHOOLNR1");
        String expectedResponseObject = "Student registered to course";

        //when
        MvcResult result = mvc.perform(post("/system/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(registerStudentRequestBodyModel)))
                .andDo(print())
                .andReturn();

        //then
        assertTrue(result.getResponse().getContentAsString().contains(expectedResponseObject));
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:db_test/school_system_db_test.sql"})
    void registerStudentToCourseThrowsBadRequestWhenStudentNotExistsInDB() throws Exception {
        //given
        RegisterStudentRequestBodyModel registerStudentRequestBodyModel = new RegisterStudentRequestBodyModel(
                "333333", "RELIGION", "SCHOOLNR1");
        String expectedResponseObject = "Object with id 333333 not found";

        //when
        MvcResult result = mvc.perform(post("/system/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(registerStudentRequestBodyModel)))
                .andDo(print())
                .andReturn();

        //then
        assertTrue(result.getResponse().getContentAsString().contains(expectedResponseObject));
        assertEquals(400, result.getResponse().getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:db_test/school_system_db_test.sql"})
    void registerStudentToCourseThrowsBadRequestWhenCourseNotExistsInDB() throws Exception {
        //given
        RegisterStudentRequestBodyModel registerStudentRequestBodyModel = new RegisterStudentRequestBodyModel(
                "999998", "PHYSICS", "SCHOOLNR2");
        String expectedResponseObject = "Object with id PHYSICS not found";

        //when
        MvcResult result = mvc.perform(post("/system/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(registerStudentRequestBodyModel)))
                .andDo(print())
                .andReturn();

        //then
        assertTrue(result.getResponse().getContentAsString().contains(expectedResponseObject));
        assertEquals(400, result.getResponse().getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:db_test/school_system_db_test.sql"})
    void registerStudentToCourseThrowsBadRequestWhenStudentGetLimitOfCourses() throws Exception {
        //given
        RegisterStudentRequestBodyModel registerStudentRequestBodyModel = new RegisterStudentRequestBodyModel(
                "999999", "RELIGION", "SCHOOLNR1");
        String expectedResponseObject = "Student has reached  maximum number of courses";

        //when
        MvcResult result = mvc.perform(post("/system/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(registerStudentRequestBodyModel)))
                .andDo(print())
                .andReturn();

        //then
        assertTrue(result.getResponse().getContentAsString().contains(expectedResponseObject));
        assertEquals(400, result.getResponse().getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:db_test/school_system_db_test.sql"})
    void registerStudentToCourseThrowsBadRequestWhenCourseGetLimitOfCourses() throws Exception {
        //given
        RegisterStudentRequestBodyModel registerStudentRequestBodyModel = new RegisterStudentRequestBodyModel(
                "999949", "MATH", "SCHOOLNR1");
        String expectedResponseObject = "Course has reached maximum number of students";

        //when
        MvcResult result = mvc.perform(post("/system/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(registerStudentRequestBodyModel)))
                .andDo(print())
                .andReturn();

        //then
        assertTrue(result.getResponse().getContentAsString().contains(expectedResponseObject));
        assertEquals(400, result.getResponse().getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:db_test/school_system_db_test.sql"})
    void registerStudentToCourseThrowsBadRequestWhenStudentIsAlreadyEnrolledToCourses() throws Exception {
        //given
        RegisterStudentRequestBodyModel registerStudentRequestBodyModel = new RegisterStudentRequestBodyModel(
                "999999", "MATH", "SCHOOLNR1");
        String expectedResponseObject = "Student already assigned to that course";

        //when
        MvcResult result = mvc.perform(post("/system/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(registerStudentRequestBodyModel)))
                .andDo(print())
                .andReturn();

        //then
        assertTrue(result.getResponse().getContentAsString().contains(expectedResponseObject));
        assertEquals(400, result.getResponse().getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:db_test/school_system_db_test.sql"})
    void unregisterStudentFromCourseWorks() throws Exception {
        //given
        RegisterStudentRequestBodyModel registerStudentRequestBodyModel = new RegisterStudentRequestBodyModel(
                "999999", "MATH", "SCHOOLNR1");
        String expectedResponseObject = "Student unregistered from course";

        //when
        MvcResult result = mvc.perform(post("/system/unregister")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(registerStudentRequestBodyModel)))
                .andDo(print())
                .andReturn();

        //then
        assertTrue(result.getResponse().getContentAsString().contains(expectedResponseObject));
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:db_test/school_system_db_test.sql"})
    void unregisterStudentToCourseThrowsBadRequestWhenStudentNotExistsInDB() throws Exception {
        //given
        RegisterStudentRequestBodyModel registerStudentRequestBodyModel = new RegisterStudentRequestBodyModel(
                "333333", "RELIGION", "SCHOOLNR1");
        String expectedResponseObject = "Object with id 333333 not found";

        //when
        MvcResult result = mvc.perform(post("/system/unregister")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(registerStudentRequestBodyModel)))
                .andDo(print())
                .andReturn();

        //then
        assertTrue(result.getResponse().getContentAsString().contains(expectedResponseObject));
        assertEquals(400, result.getResponse().getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:db_test/school_system_db_test.sql"})
    void unregisterStudentToCourseThrowsBadRequestWhenCourseNotExistsInDB() throws Exception {
        //given
        RegisterStudentRequestBodyModel registerStudentRequestBodyModel = new RegisterStudentRequestBodyModel(
                "999998", "PHYSICS", "SCHOOLNR2");
        String expectedResponseObject = "Object with id PHYSICS not found";

        //when
        MvcResult result = mvc.perform(post("/system/unregister")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(registerStudentRequestBodyModel)))
                .andDo(print())
                .andReturn();

        //then
        assertTrue(result.getResponse().getContentAsString().contains(expectedResponseObject));
        assertEquals(400, result.getResponse().getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:db_test/school_system_db_test.sql"})
    void unregisterStudentToCourseThrowsBadRequestWhenStudentIsNotAssignedToCourse() throws Exception {
        //given
        RegisterStudentRequestBodyModel registerStudentRequestBodyModel = new RegisterStudentRequestBodyModel(
                "999949", "MATH", "SCHOOLNR1");
        String expectedResponseObject = "Student is not assigned to that course";

        //when
        MvcResult result = mvc.perform(post("/system/unregister")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(registerStudentRequestBodyModel)))
                .andDo(print())
                .andReturn();

        //then
        assertTrue(result.getResponse().getContentAsString().contains(expectedResponseObject));
        assertEquals(400, result.getResponse().getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:db_test/school_system_db_test.sql"})
    void getAllStudentsFromCourseWorks() throws Exception {
        //given
        String courseName = "PE";
        String schoolName = "SCHOOLNR2";
        Set<StudentDTO> expectedResponseObject = Set.of(
                new StudentDTO("JOHN", "SMITH", LocalDate.of(2005, 4, 4),
                        "123456788", "999948"),
                new StudentDTO("GREG", "SMITH", LocalDate.of(2005, 4, 4),
                        "123456788", "999947")
        );

        //when
        MvcResult result = mvc.perform(get("/system/" + schoolName + "/" + courseName + "/students")
                .contentType("application/json"))
                .andDo(print())
                .andReturn();

        //then
        assertTrue(result.getResponse().getContentAsString().contains(
                objectMapper.writeValueAsString(expectedResponseObject)));
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:db_test/school_system_db_test.sql"})
    void getAllStudentsFromCourseThrowsBadRequestWhenCourseNotExistsInSchool() throws Exception {
        //given
        String courseName = "HISTORY";
        String schoolName = "SCHOOLNR2";
        String expectedResponseObject = "Object with id HISTORY not found";

        //when
        MvcResult result = mvc.perform(get("/system/" + schoolName + "/" + courseName + "/students")
                .contentType("application/json"))
                .andDo(print())
                .andReturn();

        //then
        assertTrue(result.getResponse().getContentAsString().contains(expectedResponseObject));
        assertEquals(404, result.getResponse().getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:db_test/school_system_db_test.sql"})
    void getAllCoursesFromStudentWorks() throws Exception {
        //given
        String studentCardID = "999949";
        Set<CourseDTO> expectedResponseObject = Set.of(
                new CourseDTO("HISTORY", "SCHOOLNR1"),
                new CourseDTO("BIOLOGY", "SCHOOLNR1")
        );

        //when
        MvcResult result = mvc.perform(get("/system/" + studentCardID + "/courses")
                .contentType("application/json"))
                .andDo(print())
                .andReturn();

        //then
        assertTrue(result.getResponse().getContentAsString().contains(
                objectMapper.writeValueAsString(expectedResponseObject)));
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:db_test/school_system_db_test.sql"})
    void getAllStudentsFromCourseThrowsBadRequestWhenStudentNotExists() throws Exception {
        //given
        String studentCardID = "000000";
        String expectedResponseObject = "Object with id 000000 not found";

        //when
        MvcResult result = mvc.perform(get("/system/" + studentCardID + "/courses")
                .contentType("application/json"))
                .andDo(print())
                .andReturn();

        //then
        assertTrue(result.getResponse().getContentAsString().contains(expectedResponseObject));
        assertEquals(404, result.getResponse().getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:db_test/school_system_db_test.sql"})
    void getAllStudentsWithoutEnrolledCoursesWorks() throws Exception {
        //given
        Set<StudentFullDTO> expectedResponseObject = Set.of(
                new StudentFullDTO("MARK", "SMITH", LocalDate.of(2005, 4, 4),
                        "123456788", "999946", Set.of()),
                new StudentFullDTO("DANIEL", "SMITH", LocalDate.of(2005, 4, 4),
                        "123456788", "999945", Set.of())
        );

        //when
        MvcResult result = mvc.perform(get("/system/students_without_courses")
                .contentType("application/json"))
                .andDo(print())
                .andReturn();

        //then
        assertTrue(result.getResponse().getContentAsString().contains(
                objectMapper.writeValueAsString(expectedResponseObject)));
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:db_test/school_system_db_test.sql"})
    void getAllCoursesWithoutEnrolledStudentsWorks() throws Exception {
        //given
        String schoolName = "SCHOOLNR1";
        Set<CourseFullDTO> expectedResponseObject = Set.of(
                new CourseFullDTO("RELIGION", "SCHOOLNR1", Set.of()),
                new CourseFullDTO("PE", "SCHOOLNR1", Set.of())
        );

        //when
        MvcResult result = mvc.perform(get("/system/" + schoolName + "/courses_without_students")
                .contentType("application/json"))
                .andDo(print())
                .andReturn();

        //then
        assertTrue(result.getResponse().getContentAsString().contains(
                objectMapper.writeValueAsString(expectedResponseObject)));
        assertEquals(200, result.getResponse().getStatus());
    }
}
