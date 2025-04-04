package ir.mrmoein.quezapplication.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ir.mrmoein.quezapplication.model.entity.Option;
import ir.mrmoein.quezapplication.model.entity.QuestionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class ExamQuestionDTO {

    private Long id;

    private String title;

    private String question;

    private double score;

    private String questionType;

    private List<String> options;

}
