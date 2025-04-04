package ir.mrmoein.quezapplication.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class ExamQuestion extends BaseEntity<Long> {

    @Column(nullable = false)
    private String title;

    private String question;

    private double score;

    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    @OneToMany(mappedBy = "examQuestion")
    private List<Option> options;

    @ManyToOne
    private Course category;

    @ManyToMany
    private List<Exam> exams;

    @PrePersist
    public void init(){
        if (this.exams == null) {
            this.exams = new ArrayList<>();
        }
    }

    public void addExam(Exam exam) {
        if (this.exams == null) {
            this.exams = new ArrayList<>();
        }
        if (!this.exams.contains(exam)) {
            this.exams.add(exam);
        }
    }
}