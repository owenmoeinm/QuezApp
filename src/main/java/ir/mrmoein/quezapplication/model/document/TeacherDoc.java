package ir.mrmoein.quezapplication.model.document;

import ir.mrmoein.quezapplication.model.entity.State;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@Document(indexName = "document_teacher")
public class TeacherDoc{

    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private String userId;

    @Field
    private String fullName;

    @Field(type = FieldType.Keyword)
    @Size(max = 10)
    private String nationalCode;

    @Email
    private String email;

    @Field(type = FieldType.Text)
    private String status = State.VALIDATING.name();

    @Field
    private String role;

    @Field(type = FieldType.Date)
    private LocalDate dob;

}
