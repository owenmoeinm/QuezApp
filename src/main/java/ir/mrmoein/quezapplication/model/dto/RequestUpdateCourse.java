package ir.mrmoein.quezapplication.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class RequestUpdateCourse {
    private String name;

    private String teacher;

    private String teacherCode;

    private MultipartFile image;

    private LocalDate startDate;

    private LocalDate endOfTerms;

    private List<String> students;

}
