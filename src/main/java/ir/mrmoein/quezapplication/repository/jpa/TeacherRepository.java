package ir.mrmoein.quezapplication.repository.jpa;

import ir.mrmoein.quezapplication.model.entity.Teacher;
import ir.mrmoein.quezapplication.model.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher , UUID> {

    @Query("select t from Teacher t where t.userId.id = :userID")
    Optional<Teacher> findByUserId (UUID userID);

    Optional<Teacher> findByNationalCode(String nationalCode);

    @Transactional
    void deleteByNationalCode(String nationalCode);

}
