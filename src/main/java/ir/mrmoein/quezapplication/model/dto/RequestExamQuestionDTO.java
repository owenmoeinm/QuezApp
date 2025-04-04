package ir.mrmoein.quezapplication.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class RequestExamQuestionDTO {

    private String title;

    private String question;

    private List<String> options;

    private String type;

    private String score;

    private String course;

    private String exam;

}
