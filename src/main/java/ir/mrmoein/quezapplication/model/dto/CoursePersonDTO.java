package ir.mrmoein.quezapplication.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class CoursePersonDTO {

    private String name;

    private String teacher;

    private LocalDate startDate;

    private LocalDate endDate;

    private String students;

    private byte[] image;

    private List<ExamDTO> exams;

}
