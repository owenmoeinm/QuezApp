package ir.mrmoein.quezapplication.model.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;


import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.Instant;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Entity
@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class Teacher extends Person {

    @Column
    private LocalDate dob;

    @OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY)
    private List<Course> courses = new LinkedList<>();

    @PrePersist
    public void init() {
        super.setStatus(State.VALIDATING);
    }

    public void setCourses(Course course) {
        this.courses.add(course);
    }
}
