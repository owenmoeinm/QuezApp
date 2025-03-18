package ir.mrmoein.quezapplication.model.dto;

import ir.mrmoein.quezapplication.model.entity.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class UserRequestDTO {

    private String username;

    private String password;

    private Set<Role> role;

}
