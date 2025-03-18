package ir.mrmoein.quezapplication.repository.elastic;

import ir.mrmoein.quezapplication.model.document.CourseDoc;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SearchCourseRepo extends ElasticsearchRepository<CourseDoc, UUID> {

    List<CourseDoc> findByName(String name);

    Optional<CourseDoc> findByNameAndStartDate(String name , LocalDate date);

}
