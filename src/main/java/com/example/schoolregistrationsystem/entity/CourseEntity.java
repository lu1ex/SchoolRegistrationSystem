package com.example.schoolregistrationsystem.entity;

import com.example.schoolregistrationsystem.requestBodyModel.CourseRequestBodyModel;
import liquibase.repackaged.org.apache.commons.lang3.StringUtils;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "courses")
public class CourseEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;
    private String name;
    private String schoolName;
    @ManyToMany(mappedBy = "courses", fetch = FetchType.EAGER)
    private Set<StudentEntity> students = new HashSet<>();

    public CourseEntity(CourseRequestBodyModel courseRequestBodyModel) {
        this.id = UUID.randomUUID().toString();
        this.name = courseRequestBodyModel.getName().toUpperCase();
        this.schoolName = courseRequestBodyModel.getSchoolName().toUpperCase();
    }
}
