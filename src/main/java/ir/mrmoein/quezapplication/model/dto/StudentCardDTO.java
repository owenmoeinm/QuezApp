package ir.mrmoein.quezapplication.model.dto;

import ir.mrmoein.quezapplication.model.entity.Exam;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class StudentCardDTO {

    private byte[] image;

    private String fullName;

    private String nationalCode;

    private String status;

    private String email;

    private List<Exam> exams;

}
