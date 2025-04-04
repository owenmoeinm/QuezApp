package ir.mrmoein.quezapplication.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class EditionProfileDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String lastName;

    @NotNull
    private String nationalCode;

    @Email
    private String email;

    private LocalDate dob;

    private MultipartFile image;
}
