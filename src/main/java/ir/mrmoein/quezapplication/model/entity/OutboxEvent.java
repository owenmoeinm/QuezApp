package ir.mrmoein.quezapplication.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Table(name = "outbox_event")
public class OutboxEvent extends BaseEntity<UUID> {

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private String fullName;

    @Column(unique = true, nullable = false)
    private String nationalCode;

    @Column(nullable = false, unique = true)
    @Email
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private State status ;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false, updatable = false)
    private LocalDate generalDate = LocalDate.now();

    @PrePersist
    public void init() {
        this.status = State.VALIDATING;
    }

}
