package ir.mrmoein.quezapplication.model.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.elasticsearch.annotations.Field;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import java.util.UUID;

@MappedSuperclass
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public abstract class Person extends BaseEntity<UUID> {

    @OneToOne(cascade = CascadeType.REMOVE)
    private User userId;

    @Column
    private String name;

    @Column
    private String lastName;


    @Column(columnDefinition = "BYTEA")
    private byte[] image;

    @Column(nullable = false , unique = true)
    @Size(max = 10)
    private String nationalCode;

    @Email
    private String email;

    @Column
    @Enumerated(EnumType.STRING)
    private State status;


}
