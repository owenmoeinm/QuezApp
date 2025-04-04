package ir.mrmoein.quezapplication.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
public class CoursesDTO {

    private String name;

    private String teacher;

    private String nationalTeacher;

    private LocalDate startDate;

    private LocalDate endOfTerms;

    private int students;


}
