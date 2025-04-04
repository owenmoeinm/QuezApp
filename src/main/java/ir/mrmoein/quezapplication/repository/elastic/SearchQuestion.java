package ir.mrmoein.quezapplication.repository.elastic;

import ir.mrmoein.quezapplication.model.document.QuestionDoc;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SearchQuestion extends ElasticsearchRepository<QuestionDoc , UUID> {

    List<QuestionDoc> findByTitleContaining(String question);

}
