package ir.mrmoein.quezapplication.model.document;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;
import java.util.List;

@Document(indexName = "course_doc")
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class CourseDoc {

    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private String name;

    @Field(type = FieldType.Text)
    private String teacher;

    @Field(type = FieldType.Date, format = DateFormat.date)
    private LocalDate startDate;

    @Field(type = FieldType.Date, format = DateFormat.date)
    private LocalDate endOfTerms;

    private List<String> students;

}
