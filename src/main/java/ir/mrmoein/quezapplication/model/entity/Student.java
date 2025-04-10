package ir.mrmoein.quezapplication.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Entity
@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class Student extends Person {

    @ManyToMany(fetch = FetchType.LAZY , mappedBy = "students" )
    private List<Course> courses = new LinkedList<>();

    @ManyToMany(fetch = FetchType.LAZY , mappedBy = "students")
    private List<Exam> exams = new LinkedList<>();

    @Column
    private LocalDate createDate;

    @OneToMany(mappedBy = "student" , fetch = FetchType.LAZY)
    private List<StudentExam> studentExams;

    @Transient
    private int temporalScore;

    @PrePersist
    public void init() {
        this.createDate = LocalDate.now();
        super.setStatus(State.VALIDATING);
    }

    public void setCourses(Course course) {
        this.courses.add(course);
    }
}
