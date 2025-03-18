package ir.mrmoein.quezapplication.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Role extends BaseEntity<Integer>{

    @Column(nullable = false)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY , cascade = {CascadeType.PERSIST , CascadeType.REMOVE})
    @JoinTable(
            name = "role_authority",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id")
    )
    private Set<Authority> authorities = new HashSet<>();

}
