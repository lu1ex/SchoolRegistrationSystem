package com.example.schoolregistrationsystem.entity;

import com.example.schoolregistrationsystem.requestBodyModel.StudentRequestBodyModel;
import liquibase.repackaged.org.apache.commons.lang3.StringUtils;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "students")
public class StudentEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;
    private String name;
    private String surname;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    @Column(name = "student_card_ID")
    private String studentCardID;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "students_enrolled_to_courses",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    private Set<CourseEntity> courses = new HashSet<>();

    public StudentEntity(StudentRequestBodyModel studentRequestBodyModel) {
        this.id = UUID.randomUUID().toString();
        this.name = studentRequestBodyModel.getName().toUpperCase();
        this.surname = studentRequestBodyModel.getSurname().toUpperCase();
        this.dateOfBirth = studentRequestBodyModel.getDateOfBirth();
        this.phoneNumber = studentRequestBodyModel.getPhoneNumber();
        this.studentCardID = studentRequestBodyModel.getStudentCardID();
        this.courses = new HashSet<>();
    }
}
