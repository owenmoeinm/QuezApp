package ir.mrmoein.quezapplication.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
public class QuestionCorrectionDTO {

    private String id;

    private String question;

    private String answer;

    private String answerId;

    private String score;

}
