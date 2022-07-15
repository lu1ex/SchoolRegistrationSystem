package com.example.schoolregistrationsystem;

import com.example.schoolregistrationsystem.DTO.StudentFullDTO;
import com.example.schoolregistrationsystem.entity.StudentEntity;
import com.example.schoolregistrationsystem.exceptions.ValueNotFoundException;
import com.example.schoolregistrationsystem.repository.StudentEntityRepository;
import com.example.schoolregistrationsystem.requestBodyModel.StudentRequestBodyModel;
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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class StudentEntityControllerIntegrationTest {

    @Inject
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StudentEntityRepository studentEntityRepository;

    @Autowired
    private StudentEntityService studentEntityService;

    @Test
    @Sql(scripts = {"classpath:db_test/student_and_course_crud_db_test.sql"})
    void createStudentEntityIntegrationTestWorks() throws Exception {
        //given
        StudentRequestBodyModel studentToCreateBodyModel = new StudentRequestBodyModel(
                "Name", "Surname", LocalDate.of(1997, 4, 4),
                "500500500", "100100");
        StudentFullDTO expectedResponseObject = new StudentFullDTO("NAME", "SURNAME",
                LocalDate.of(1997, 4, 4), "500500500",
                "100100", Set.of());
        int numberOfStudentsInDBBefore = studentEntityRepository.findAll().size();

        //when
        MvcResult result = mvc.perform(post("/student")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(studentToCreateBodyModel)))
                .andDo(print())
                .andReturn();

        int numberOfStudentsInDBAfter = studentEntityRepository.findAll().size();

        //then
        assertEquals(numberOfStudentsInDBAfter, (numberOfStudentsInDBBefore + 1));
        assertTrue(result.getResponse().getContentAsString().contains(objectMapper.writeValueAsString(expectedResponseObject)));
        assertEquals(201, result.getResponse().getStatus());
    }

    @Test //throws StudentCardIDAlreadyExistsException
    @Sql(scripts = {"classpath:db_test/student_and_course_crud_db_test.sql"})
    void createStudentEntityIntegrationTestThrowsBadRequestWhenStudentCardIDAlreadyExistsInDB() throws Exception {
        //given
        StudentRequestBodyModel studentToCreateBodyModel = new StudentRequestBodyModel(
                "Name", "Surname",  LocalDate.of(1997, 4, 4),
                "500500500", "999999");
        int numberOfStudentsInDBBefore = studentEntityRepository.findAll().size();

        //when
        MvcResult result = mvc.perform(post("/student")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(studentToCreateBodyModel)))
                .andDo(print())
                .andReturn();

        int numberOfStudentsInDBAfter = studentEntityRepository.findAll().size();

        //then
        assertEquals(numberOfStudentsInDBAfter, numberOfStudentsInDBBefore);
        assertTrue(result.getResponse().getContentAsString()
                .contains("There is already student with that student card ID."));
        assertEquals(400, result.getResponse().getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:db_test/student_and_course_crud_db_test.sql"})
    void getStudentEntityByIdWorks() throws Exception {
        //given
        String studentID = "00000000000000000000000000000001";
        StudentFullDTO expectedResponseObject = new StudentFullDTO("JOHN", "SMITH",
                LocalDate.of(2002, 4, 4),  "123456789",
                "999999", Set.of());

        //when
        MvcResult result = mvc.perform(get("/student/" + studentID))
                .andDo(print())
                .andReturn();

        //then
        assertTrue(result.getResponse().getContentAsString().contains(objectMapper.writeValueAsString(expectedResponseObject)));
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:db_test/student_and_course_crud_db_test.sql"})
    void getStudentEntityByIdThrowsBadRequestWhenStudentNotExistsInDB() throws Exception {
        //given
        String studentID = "0000000000000000000000idNotExist";

        //when
        MvcResult result = mvc.perform(get("/student/" + studentID))
                .andDo(print())
                .andReturn();

        //then
        assertTrue(result.getResponse().getContentAsString()
                .contains("Object with id 0000000000000000000000idNotExist not found"));
        assertEquals(404, result.getResponse().getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:db_test/student_and_course_crud_db_test.sql"})
    void getStudentEntityByStudentCardIDWorks() {
        //given
        String studentCardID = "999999";
        StudentEntity expectedStudentEntity = new StudentEntity("00000000000000000000000000000001", "JOHN",
                "SMITH", LocalDate.of(2002, 4, 4),"123456789",
                "999999", Set.of());

        //when
        StudentEntity result = studentEntityService.getStudentEntityByStudentCardID(studentCardID);

        //then
        assertEquals(expectedStudentEntity.toString(), result.toString());
    }

    @Test
    @Sql(scripts = {"classpath:db_test/student_and_course_crud_db_test.sql"})
    void getStudentEntityByStudentCardIDThrowsBadRequestWhenStudentNotExistsInDB() {
        //given
        String studentCardID = "777777";

        //then
        assertThrows(ValueNotFoundException.class, () -> studentEntityService.getStudentEntityByStudentCardID(studentCardID));
    }

    @Test
    @Sql(scripts = {"classpath:db_test/student_and_course_crud_db_test.sql"})
    void getAllStudentEntitiesWorks() throws Exception {
        //given
        Set<StudentFullDTO> expectedResponseObject = Set.of(
                new StudentFullDTO("ADAM", "SMITH", LocalDate.of(2005, 4, 4),
                        "123456788", "999998", Set.of()),
                new StudentFullDTO("JOHN", "SMITH", LocalDate.of(2002, 4, 4),
                        "123456789", "999999", Set.of()));

        //when
        MvcResult result = mvc.perform(get("/student/all"))
                .andDo(print())
                .andReturn();

        //then
        assertTrue(result.getResponse().getContentAsString().contains(objectMapper.writeValueAsString(expectedResponseObject)));
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:db_test/student_and_course_crud_db_test.sql"})
    void deleteStudentEntityByIdWorks() throws Exception {
        //given
        String studentID = "00000000000000000000000000000002";
        int numberOfStudentsInDBBefore = studentEntityRepository.findAll().size();

        //when
        MvcResult result = mvc.perform(delete("/student/" + studentID))
                .andDo(print())
                .andReturn();
        int numberOfStudentsInDBAfter = studentEntityRepository.findAll().size();

        //then
        assertTrue(numberOfStudentsInDBAfter < numberOfStudentsInDBBefore);
       assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:db_test/student_and_course_crud_db_test.sql"})
    void deleteStudentEntityThrowsExceptionWhenStudentNotExists() throws Exception {
        //given
        String studentID = "000000000000000000000idNotExists";
        int numberOfStudentsInDBBefore = studentEntityRepository.findAll().size();

        //when
        MvcResult result = mvc.perform(delete("/student/" + studentID))
                .andDo(print())
                .andReturn();
        int numberOfStudentsInDBAfter = studentEntityRepository.findAll().size();

        //then
        assertEquals(numberOfStudentsInDBAfter, numberOfStudentsInDBBefore);
        assertTrue(result.getResponse().getContentAsString()
                .contains("Object with id 000000000000000000000idNotExists not found"));
        assertEquals(404, result.getResponse().getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:db_test/student_and_course_crud_db_test.sql"})
    void putStudentEntityWorks() throws Exception {
        //given
        String studentID = "00000000000000000000000000000001";
        StudentRequestBodyModel studentRequestBodyModel = new StudentRequestBodyModel();
        studentRequestBodyModel.setName("Mark"); //was John
        studentRequestBodyModel.setSurname("Changedsurname");
        studentRequestBodyModel.setDateOfBirth(LocalDate.of(1995, 4, 4));
        studentRequestBodyModel.setStudentCardID("121212");
        studentRequestBodyModel.setPhoneNumber("666666666");

        StudentFullDTO expectedResponseObject = new StudentFullDTO("Mark", "Changedsurname",
                LocalDate.of(1995, 4, 4),"666666666",
                "121212", new HashSet<>());

        //when
        MvcResult result = mvc.perform(put("/student/" + studentID)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(studentRequestBodyModel)))
                .andDo(print())
                .andReturn();

        //then
        assertTrue(result.getResponse().getContentAsString().contains(objectMapper.writeValueAsString(expectedResponseObject)));
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:db_test/student_and_course_crud_db_test.sql"})
    void putStudentEntityThrowsExceptionWhenStudentNotExists() throws Exception {
        //given
        String studentID = "000000000000000000000idNotExists";
        StudentRequestBodyModel studentRequestBodyModel = new StudentRequestBodyModel();
        studentRequestBodyModel.setName("ChangedName"); //was John
        studentRequestBodyModel.setSurname("ChangedSurname");
        studentRequestBodyModel.setDateOfBirth(LocalDate.of(1995, 4, 4));
        studentRequestBodyModel.setStudentCardID("121212");
        studentRequestBodyModel.setPhoneNumber("666666666");

        //when
        MvcResult result = mvc.perform(put("/student/" + studentID)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(studentRequestBodyModel)))
                .andDo(print())
                .andReturn();

        //then
        assertTrue(result.getResponse().getContentAsString()
                .contains("Object with id 000000000000000000000idNotExists not found"));
        assertEquals(404, result.getResponse().getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:db_test/student_and_course_crud_db_test.sql"})
    void patchStudentEntityWorks() throws Exception {
        //given
        String studentID = "00000000000000000000000000000001";
        Map<String, String> fieldtoChangeAndNewValue = Map.of("name", "GREG");

        StudentFullDTO expectedResponseObject = new StudentFullDTO("GREG", "SMITH",
                LocalDate.of(2002, 4, 4),"123456789",
                "999999", Set.of());

        //when
        MvcResult result = mvc.perform(patch("/student/" + studentID)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(fieldtoChangeAndNewValue)))
                .andDo(print())
                .andReturn();

        //then
        assertTrue(result.getResponse().getContentAsString().contains(objectMapper.writeValueAsString(expectedResponseObject)));
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:db_test/student_and_course_crud_db_test.sql"})
    void patchStudentEntityThrowsExceptionWhenStudentNotExists() throws Exception {
        //given
        String studentID = "000000000000000000000idNotExists";
        Map<String, String> fieldtoChangeAndNewValue = Map.of("name", "GREG");

        //when
        MvcResult result = mvc.perform(patch("/student/" + studentID)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(fieldtoChangeAndNewValue)))
                .andDo(print())
                .andReturn();

        //then
        assertTrue(result.getResponse().getContentAsString()
                .contains("Object with id 000000000000000000000idNotExists not found"));
        assertEquals(404, result.getResponse().getStatus());
    }
}
