package ir.mrmoein.quezapplication.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
public class ProfileDTO {

    private String fullName;

    private String nationalCode;

    private String State;

    private String email;

    private byte[] image;

}
