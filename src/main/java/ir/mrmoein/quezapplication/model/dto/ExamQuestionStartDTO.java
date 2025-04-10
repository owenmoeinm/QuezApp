package ir.mrmoein.quezapplication.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class ExamQuestionStartDTO {

    private Long id;

    private String question;

    private double score;

    private String questionType;

    private List<OptionDTO> options;

}
