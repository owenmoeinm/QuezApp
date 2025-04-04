package ir.mrmoein.quezapplication.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class TeacherProfileDTO {

    private String name;

    private String lastName;

    private String nationalCode;

    private String email;

    private LocalDate dob;

    private byte[] image;

}
