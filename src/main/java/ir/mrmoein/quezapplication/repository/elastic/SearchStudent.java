package ir.mrmoein.quezapplication.repository.elastic;

import ir.mrmoein.quezapplication.model.document.StudentDoc;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SearchStudent extends ElasticsearchRepository<StudentDoc, UUID> {

    Optional<StudentDoc> findByNationalCode (String nationalCode);

    List<StudentDoc> findByStatus (String state);

    List<StudentDoc> findByFullNameContains(String name);

    List<StudentDoc> findByNationalCodeContaining(String nationalCode);

    void deleteByNationalCode(String nationalCode);

}
