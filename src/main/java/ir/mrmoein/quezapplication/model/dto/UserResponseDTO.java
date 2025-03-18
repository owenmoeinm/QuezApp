package ir.mrmoein.quezapplication.model.dto;

import ir.mrmoein.quezapplication.model.entity.RoleName;
import ir.mrmoein.quezapplication.model.entity.State;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class UserResponseDTO {

    private String id;

    private String username;

    private State status;

    private RoleName role;
}
