package ir.mrmoein.quezapplication.model.document;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@Document(indexName = "document_question")
public class QuestionDoc {

    @Id
    private String id;

    private Long examQuestionId;

    @Field(type = FieldType.Keyword)
    private String title;

    @Field(type = FieldType.Text)
    private String question;

    private String questionType;

    private double score;

}
