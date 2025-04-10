package ir.mrmoein.quezapplication.model.dto;

import ir.mrmoein.quezapplication.model.entity.ExamQuestion;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class ExamStartDTO {

    private String id;

    private String examName;

    private Long duration;

    private String studentExam;

    private List<ExamQuestionStartDTO> examQuestions;

}
