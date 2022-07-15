package com.example.schoolregistrationsystem;

import com.example.schoolregistrationsystem.entity.StudentEntity;
import com.example.schoolregistrationsystem.exceptions.StudentCardIDAlreadyExistsException;
import com.example.schoolregistrationsystem.exceptions.ValueNotFoundException;
import com.example.schoolregistrationsystem.repository.StudentEntityRepository;
import com.example.schoolregistrationsystem.requestBodyModel.StudentRequestBodyModel;
import com.example.schoolregistrationsystem.service.StudentEntityService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UnitTestStudentEntityService {
    @Autowired
    private StudentEntityService studentEntityService;

    @MockBean
    private StudentEntityRepository studentEntityRepository;

    @Test
    public void createStudentEntityWorksUnitTest() {
        //given
        StudentRequestBodyModel studentRequestBodyModel = new StudentRequestBodyModel("Name", "Surname",
                LocalDate.of(2005, 1, 1), "123456789", "100100");
        StudentEntity expectedResult = new StudentEntity("ID", "Name", "Surname",
                LocalDate.of(2005, 1, 1), "123456789", "100100", Set.of());

        //when
        Mockito.when(studentEntityRepository.findByStudentCardID(Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(studentEntityRepository.saveAndFlush(Mockito.any())).thenReturn(
                new StudentEntity("ID", "Name", "Surname",
                        LocalDate.of(2005, 1, 1), "123456789", "100100", Set.of()));
        StudentEntity result = studentEntityService.createStudentEntity(studentRequestBodyModel);

        //then
        assertEquals(result.toString(), expectedResult.toString());
    }

    @Test(expected = StudentCardIDAlreadyExistsException.class)
    public void createStudentEntityThrowExceptionWhenStudentCardIDAlreadyExistsTest() {
        //given
        StudentRequestBodyModel studentRequestBodyModel = new StudentRequestBodyModel("Name", "Surname",
                LocalDate.of(2005, 1, 1), "123456789", "100100");

        //when
        Mockito.when(studentEntityRepository.findByStudentCardID(Mockito.anyString())).thenReturn(Optional.of(
                new StudentEntity("ID", "Name", "Surname",
                LocalDate.of(2007, 1, 1), "987654321", "100100", Set.of())));
        StudentEntity result = studentEntityService.createStudentEntity(studentRequestBodyModel);

        //given
        //Throws StudentCardIDAlreadyExistsException because there is already Student with that student card ID
    }

    @Test
    public void getStudentEntityByIdWorksUnitTest() {
        //given
        String studentID = "000000000000000000000000000000id";

        StudentEntity expectedResult = new StudentEntity("ID", "Name", "Surname",
                LocalDate.of(2005, 1, 1), "123456789", "100100", Set.of());

        //when
        Mockito.when(studentEntityRepository.findById(Mockito.anyString())).thenReturn(
                Optional.of(new StudentEntity("ID", "Name", "Surname",
                        LocalDate.of(2005, 1, 1), "123456789", "100100", Set.of())));
        StudentEntity result = studentEntityService.getStudentEntityById(studentID);

        //then
        assertEquals(result.toString(), expectedResult.toString());
    }

    @Test(expected = ValueNotFoundException.class)
    public void getStudentEntityByIdThrowExceptionWhenIDNotExistsUnitTest() {
        //given
        String studentID = "000000000000000000000idNotExists";

        //when
        Mockito.when(studentEntityRepository.findById(Mockito.anyString())).thenThrow(ValueNotFoundException.class);
        StudentEntity result = studentEntityService.getStudentEntityById(studentID);

        //given
        //Throws ValueNotFoundException because repository does not find Student by id
    }

    @Test
    public void getStudentEntityByStudentCardIDWorksUnitTest() {
        //given
        String studentCardID = "100100";
        StudentEntity expectedResult = new StudentEntity("ID", "Name", "Surname",
                LocalDate.of(2005, 1, 1), "123456789", "100100", Set.of());

        //when
        Mockito.when(studentEntityRepository.findByStudentCardID(Mockito.anyString())).thenReturn(
                Optional.of(new StudentEntity("ID", "Name", "Surname",
                        LocalDate.of(2005, 1, 1), "123456789", "100100", Set.of())));
        StudentEntity result = studentEntityService.getStudentEntityByStudentCardID(studentCardID);

        //then
        assertEquals(result.toString(), expectedResult.toString());
    }

    @Test(expected = ValueNotFoundException.class)
    public void getStudentEntityByStudentCardIDThrowExceptionWhenStudentNotExistsUnitTest() {
        //given
        String studentID = "999999";

        //when
        Mockito.when(studentEntityRepository.findByStudentCardID(Mockito.anyString()))
                .thenThrow(ValueNotFoundException.class);
        StudentEntity result = studentEntityService.getStudentEntityByStudentCardID(studentID);

        //given
        //Throws ValueNotFoundException because repository does not find Student by Student card ID
    }

    @Test
    public void getAllStudentsEntitiesWorksUnitTest() {
        //given
        Set<StudentEntity> expectedSetOfEntity = Set.of(
                new StudentEntity("ID1", "Name1", "Surname1", LocalDate.of(2005, 1, 1),
                        "123456789", "100100", Set.of()),
                new StudentEntity("ID2", "Name2", "Surname2", LocalDate.of(2005, 11, 11),
                        "987654321", "100101", Set.of()));

        //when
        Mockito.when(studentEntityRepository.findAll()).thenReturn(
                List.of( new StudentEntity("ID1", "Name1", "Surname1", LocalDate.of(2005, 1, 1),
                                "123456789", "100100", Set.of()),
                        new StudentEntity("ID2", "Name2", "Surname2", LocalDate.of(2005, 11, 11),
                                "987654321", "100101", Set.of())));
        Set<StudentEntity> result = studentEntityService.getAllStudentEntities();

        //then
        assertEquals(result.toString(), expectedSetOfEntity.toString());
    }

    @Test
    public void deleteStudentEntityByIdWorksUnitTest() {
        //given
        String studentID = "000000000000000000000000000000id";

        //when
        Mockito.when(studentEntityRepository.findById(Mockito.anyString())).thenReturn(Optional.of(
                new StudentEntity("ID", "Name", "Surname",
                LocalDate.of(2005, 1, 1), "123456789", "100100", Set.of())));
        studentEntityService.deleteStudentEntityById(studentID);

        //then
    }

    @Test(expected = ValueNotFoundException.class)
    public void deleteStudentEntityByIdThrowExceptionWhenIDNotExistsUnitTest() {
        //given
        String studentID = "000000000000000000000idNotExists";

        //when
        Mockito.when(studentEntityService.getStudentEntityById(Mockito.anyString())).thenThrow(ValueNotFoundException.class);
        studentEntityService.deleteStudentEntityById(studentID);

        //given
        //Throws ValueNotFoundException because repository does not find Student by id
    }

    @Test
    public void putStudentEntityWorksUnitTest() {
        //given
        String studentID = "000000000000000000000000000000id";
        StudentRequestBodyModel studentRequestBodyModel = new StudentRequestBodyModel("NameModified",
                "SurnameModified", LocalDate.of(2010, 8, 18), "666555444",
                "100111");
        StudentEntity studentEntityBefore = new StudentEntity("ID", "Name", "Surname",
                LocalDate.of(2005, 1, 1), "123456789", "100100", Set.of());

        //when
        Mockito.when(studentEntityRepository.findById(Mockito.anyString())).thenReturn(Optional.of(new StudentEntity(
                "000000000000000000000000000000id", "NameModified", "SurnameModified",
                LocalDate.of(2010, 8, 18), "666555444", "100111", Set.of())));
        StudentEntity result = studentEntityService.putStudentEntity(studentID, studentRequestBodyModel);

        //then
        assertNotEquals(result.getName(), studentEntityBefore.getName());
        assertNotEquals(result.getSurname(), studentEntityBefore.getSurname());
        assertEquals(result.getName(), studentRequestBodyModel.getName());
        assertEquals(result.getStudentCardID(), studentRequestBodyModel.getStudentCardID());
        assertEquals(result.getPhoneNumber(), studentRequestBodyModel.getPhoneNumber());
    }

    @Test(expected = ValueNotFoundException.class)
    public void putStudentEntityThrowExceptionWhenStudentNotExistsTest() {
        //given
        String studentID = "000000000000000000000000000000id";
        StudentRequestBodyModel studentRequestBodyModel = new StudentRequestBodyModel("NameModified",
                "SurnameModified", LocalDate.of(2010, 8, 18), "666555444",
                "100111");

        //when
        Mockito.when(studentEntityRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
        StudentEntity result = studentEntityService.putStudentEntity(studentID, studentRequestBodyModel);

        //given
        //Throws ValueNotFoundException because repository does not find Student by ID
    }

    @Test
    public void patchStudentEntityWorksUnitTest() {
        //given
        String studentID = "000000000000000000000000000000id";
        Map<Object, Object> fieldsToChange = Map.of("name", "ModifiedName");
        StudentEntity studentEntityBefore = new StudentEntity("ID", "Name", "Surname",
                LocalDate.of(2005, 1, 1), "123456789", "100100", Set.of());

        //when
        Mockito.when(studentEntityRepository.findById(Mockito.anyString())).thenReturn(Optional.of(new StudentEntity(
                "ID", "Name", "Surname", LocalDate.of(2005, 1, 1),
                "123456789", "100100", Set.of())));
        StudentEntity result = studentEntityService.patchStudentEntity(studentID, fieldsToChange);

        //then
        assertNotEquals(result.getName(), studentEntityBefore.getName());
        assertEquals(result.getName(), fieldsToChange.get("name"));
        assertEquals(result.getSurname(), studentEntityBefore.getSurname());
    }

    @Test(expected = ValueNotFoundException.class)
    public void patchStudentEntityThrowExceptionWhenStudentNotExistsTest() {
        //given
        String studentID = "000000000000000000000000000000id";
        Map<Object, Object> fieldsToChange = Map.of("name", "ModifiedName");

        //when
        Mockito.when(studentEntityRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
        StudentEntity result = studentEntityService.patchStudentEntity(studentID, fieldsToChange);

        //given
        //Throws ValueNotFoundException because repository does not find Student by ID
    }
}
