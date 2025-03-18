package ir.mrmoein.quezapplication.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Authority extends BaseEntity<Long>{

    @NotBlank
    @Column(nullable = false)
    private String name;


}
