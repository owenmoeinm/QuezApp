package ir.mrmoein.quezapplication.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
public class ExamSubmitDTO {

    private String examId;

    private Map<String , String> answers;

    private String studentExam;

}
