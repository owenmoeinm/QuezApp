package ir.mrmoein.quezapplication.model.dto;

import ir.mrmoein.quezapplication.model.entity.CurrentExam;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
public class ExamDTO {

    @NotBlank
    private Long id;

    @NotBlank
    private String name;

    private String description;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Long duration;

    private CurrentExam state;

    @PrePersist
    public void init() {
        this.state = CurrentExam.UPCOMING;
    }

}
