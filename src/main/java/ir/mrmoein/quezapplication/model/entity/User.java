package ir.mrmoein.quezapplication.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.util.*;

@Entity
@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@Table(name = "UserApp")
public class User extends BaseEntity<UUID>{

    @Column(nullable = false , unique = true)
    @Size(min = 4 , max = 12)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column
    private Boolean enable;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

}
