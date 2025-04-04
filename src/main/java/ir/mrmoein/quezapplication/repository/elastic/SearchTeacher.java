package ir.mrmoein.quezapplication.repository.elastic;

import ir.mrmoein.quezapplication.model.document.TeacherDoc;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SearchTeacher extends ElasticsearchRepository<TeacherDoc, UUID> {

    Optional<TeacherDoc> findByNationalCode (String nationalCode);

    List<TeacherDoc> findByStatus (String state);

    List<TeacherDoc> findByFullNameContains(String fullName);

    List<TeacherDoc> findByNationalCodeContaining(String nationalCode);

    void deleteByNationalCode(String nationalCode);

}
