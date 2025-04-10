package ir.mrmoein.quezapplication.repository.jpa;

import ir.mrmoein.quezapplication.model.entity.AnswerQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerQuestionRepository extends JpaRepository<AnswerQuestion, Long> {


}
