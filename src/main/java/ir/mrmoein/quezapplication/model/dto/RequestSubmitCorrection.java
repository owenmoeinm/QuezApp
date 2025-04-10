package ir.mrmoein.quezapplication.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class RequestSubmitCorrection {

    private String examStudentID;

    private String score;

    private String answerId;
}
