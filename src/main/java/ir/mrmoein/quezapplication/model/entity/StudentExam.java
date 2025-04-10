package ir.mrmoein.quezapplication.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Table(name = "student_exam")
public class StudentExam extends BaseEntity<Long>{

    @ManyToOne
    private Student student;

    @ManyToOne
    private Exam exam;

    private double grade;

    private LocalTime startTime;

    private LocalTime sendTime;

    @Enumerated(EnumType.STRING)
    private Visit visit;

    @OneToMany(mappedBy = "studentExam")
    private List<AnswerQuestion> answerQuestions;

    @PrePersist
    public void prePersist(){
        startTime = LocalTime.now();
    }

}
