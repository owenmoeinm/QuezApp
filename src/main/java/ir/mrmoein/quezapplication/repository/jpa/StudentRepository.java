package ir.mrmoein.quezapplication.repository.jpa;

import ir.mrmoein.quezapplication.model.entity.Student;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {

    @Query("select s from Student s where s.nationalCode = :nationalCode")
    Optional<Student> findByNationalCode (String nationalCode);

    @Transactional
    void deleteByNationalCode(String nationalCode);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM course_students s WHERE s.students_id = :studentId", nativeQuery = true)
    void deleteStudentFromJoinTable(UUID studentId);

}
