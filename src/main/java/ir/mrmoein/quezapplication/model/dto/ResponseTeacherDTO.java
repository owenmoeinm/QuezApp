package ir.mrmoein.quezapplication.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Builder
@Getter
@Setter
public class ResponseTeacherDTO {

    private String name;

    private String lastName;

    private byte[] image;

    private String email;

    private String nationalCode;

    private String phone;

    private LocalDate dop;

    private int courses;

}
