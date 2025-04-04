package ir.mrmoein.quezapplication.repository.jpa;

import ir.mrmoein.quezapplication.model.entity.Exam;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {

    @Transactional
    void deleteById(Long id);

    Optional<Exam> findById(Long id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM exam_question_exams WHERE questions_id = :questionId AND exams_id = :examId", nativeQuery = true)
    void deleteExamQuestionRelation(@Param("questionId") Long questionId, @Param("examId") Long examId);

}
