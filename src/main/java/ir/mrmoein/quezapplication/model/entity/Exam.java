package ir.mrmoein.quezapplication.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
@Entity
public class Exam extends BaseEntity<Long> {

    @NotNull
    private String name;

    private String description;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Long duration;

    @OneToMany(mappedBy = "exam" , fetch = FetchType.LAZY)
    private List<StudentExam> studentExam;

    @ManyToOne
    private Course course;

    @Enumerated(EnumType.STRING)
    private Visit visit;

    @Enumerated(EnumType.STRING)
    private CurrentExam state;

    @ManyToMany
    private List<Student> students;

    @ManyToMany(mappedBy = "exams")
    private List<ExamQuestion> questions = new ArrayList<>();

    public void addQuestions(ExamQuestion questions) {
        this.questions.add(questions);
    }

    @PrePersist
    public void init() {
        this.visit = Visit.UNSEEN;
        this.state = CurrentExam.UPCOMING;
    }

    public void addStudent(Student students) {
        this.students.add(students);
    }
}
