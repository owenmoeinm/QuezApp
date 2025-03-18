package ir.mrmoein.quezapplication.model.dto;

import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class ResponseCourseDTO {

    private String name;

    private String teacher;

    private String teacherCode;

    private byte[] image;

    private LocalDate startDate;

    private LocalDate endOfTerms;

    private List<String> students = new LinkedList<>();

    @PrePersist
    private void init() {
        this.teacher = "No Teacher";
    }

}
