package ir.mrmoein.quezapplication.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class TeacherRegisterRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private String name;

    private String lastname;

    @NotBlank
    @Size(max = 10)
    private String nationalCode;

    @Email
    private String email;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;

    private MultipartFile image;
}
