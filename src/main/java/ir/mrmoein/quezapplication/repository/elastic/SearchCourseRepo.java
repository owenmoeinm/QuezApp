package ir.mrmoein.quezapplication.repository.elastic;

import ir.mrmoein.quezapplication.model.document.CourseDoc;
import jakarta.transaction.Transactional;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SearchCourseRepo extends ElasticsearchRepository<CourseDoc, UUID> {

    Optional<CourseDoc> findByName(String name);

    Optional<CourseDoc> findByNameAndStartDate(String name , LocalDate date);

    @Transactional(rollbackOn = Exception.class)
    void deleteByName(String name);

}
