package ir.mrmoein.quezapplication.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class CoursesTeacherDTO {

    private String image;

    private String name;

    private LocalDate startDate;

    private LocalDate endOfTerms;

    private int students;

}
