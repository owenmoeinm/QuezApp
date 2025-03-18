package ir.mrmoein.quezapplication.model.dto;



import ir.mrmoein.quezapplication.model.entity.Course;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@Setter
public class RequestTeacherDTO {

    @NotBlank
    @NotNull
    private String name;

    @NotBlank
    @NotNull
    private String lastName;

    private byte[] image;

    @Email
    private String email;

    private String nationalCode;

    @Pattern(regexp = "^(\\+98|0)?9\\d{9}$" ,
            message = "Please enter a 9 length phone number and start with +98 or 0 or 9")
    private String phone;

    private LocalDate dop;

    private List<Course> courses;

}
