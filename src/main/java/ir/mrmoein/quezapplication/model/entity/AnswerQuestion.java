package ir.mrmoein.quezapplication.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class AnswerQuestion extends BaseEntity<Long>{

    @ManyToOne
    private ExamQuestion examQuestion;

    @ManyToOne
    private StudentExam studentExam;

    private String answer;

    @Enumerated(EnumType.STRING)
    private Visit visit;

    private double grade;

}
