package ir.mrmoein.quezapplication.repository.jpa;

import ir.mrmoein.quezapplication.model.entity.Exam;
import ir.mrmoein.quezapplication.model.entity.Student;
import ir.mrmoein.quezapplication.model.entity.StudentExam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentExamRepository extends JpaRepository<StudentExam, Long> {

    @Query("select s from StudentExam s where s.student.id = :studentId")
    Optional<StudentExam> findByStudentId(@Param("studentId") UUID id);

    Optional<StudentExam> findByStudentAndExam(Student student, Exam exam);

    List<StudentExam> findAllByStudent(Student student);

}
