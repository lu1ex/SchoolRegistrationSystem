package com.example.schoolregistrationsystem.service;

import com.example.schoolregistrationsystem.entity.CourseEntity;
import com.example.schoolregistrationsystem.entity.StudentEntity;
import com.example.schoolregistrationsystem.exceptions.CourseReachedMaxNumberOfStudentsException;
import com.example.schoolregistrationsystem.exceptions.StudentAlreadyAssignedToCourseException;
import com.example.schoolregistrationsystem.exceptions.StudentNotAssignedToCourseException;
import com.example.schoolregistrationsystem.exceptions.StudentReachedMaxNumberOfCourserException;
import com.example.schoolregistrationsystem.repository.CourseEntityRepository;
import com.example.schoolregistrationsystem.repository.StudentEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SchoolSystemService {
    private final StudentEntityService studentEntityService;
    private final StudentEntityRepository studentEntityRepository;
    private final CourseEntityService courseEntityService;
    private final CourseEntityRepository courseEntityRepository;

    public String registerStudentToCourse(String studentCardID, String courseName, String schoolName) {
        StudentEntity student = studentEntityService.getStudentEntityByStudentCardID(studentCardID);
        CourseEntity course = courseEntityService.getCourseEntityByNameAndSchoolName(courseName, schoolName);

        if (student.getCourses().contains(course)) {
            throw new StudentAlreadyAssignedToCourseException();
        } else if (student.getCourses().size() == 5) {
            throw new StudentReachedMaxNumberOfCourserException();
        } else if (course.getStudents().size() == 50) {
            throw new CourseReachedMaxNumberOfStudentsException();
        } else {
            student.getCourses().add(course);
            studentEntityRepository.saveAndFlush(student);
            return "Student registered to course";
        }
    }

    public String unregisterStudentFromCourse(String studentCardID, String courseName, String schoolName) {
        StudentEntity student = studentEntityService.getStudentEntityByStudentCardID(studentCardID);
        CourseEntity course = courseEntityService.getCourseEntityByNameAndSchoolName(courseName, schoolName);

        if (student.getCourses().contains(course)) {
            student.getCourses().remove(course);
            studentEntityRepository.saveAndFlush(student);
            return "Student unregistered from course";
        } else {
            throw new StudentNotAssignedToCourseException();
        }
    }

    public Set<StudentEntity> getAllStudentsFromCourse(String courseName, String schoolName) {
        return courseEntityService.getCourseEntityByNameAndSchoolName(courseName, schoolName).getStudents();
    }

    public Set<CourseEntity> getAllCoursesFromStudent(String studentCardID) {
        return studentEntityService.getStudentEntityByStudentCardID(studentCardID).getCourses();
    }

    public Set<StudentEntity> getAllStudentsWithoutEnrolledCourses() {
        return studentEntityService.getAllStudentEntities().stream()
                .filter(studentEntity -> studentEntity.getCourses().isEmpty())
                .collect(Collectors.toSet());
    }

    public Set<CourseEntity> getAllCoursesWithoutEnrolledStudents(String schoolName) {
        return courseEntityService.getAllCourseEntitiesBySchoolName(schoolName).stream()
                .filter(courseEntity -> courseEntity.getStudents().isEmpty())
                .collect(Collectors.toSet());
    }
}
