package ir.mrmoein.quezapplication.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter @Setter
@NoArgsConstructor
@SuperBuilder
public class Option extends BaseEntity<Long>{

    private String value;

    @Enumerated(EnumType.STRING)
    private CorrectionType correction;

    @ManyToOne
    private ExamQuestion examQuestion;

    @PrePersist
    public void init(){
        this.correction = CorrectionType.WRONG;
    }

}
