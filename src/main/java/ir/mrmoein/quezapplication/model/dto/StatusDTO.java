package ir.mrmoein.quezapplication.model.dto;

import ir.mrmoein.quezapplication.model.entity.RoleName;
import ir.mrmoein.quezapplication.model.entity.State;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
public class StatusDTO {

    private String fullName;

    private String nationalCode;

    private State status;

    private RoleName roleName;
}
