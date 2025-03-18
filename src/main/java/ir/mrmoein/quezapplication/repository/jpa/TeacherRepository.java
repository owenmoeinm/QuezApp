package ir.mrmoein.quezapplication.repository.jpa;

import ir.mrmoein.quezapplication.model.entity.State;
import ir.mrmoein.quezapplication.model.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher , UUID> {

    List<Teacher> findByStatus (State state);

    Optional<Teacher> findByNationalCode(String nationalCode);


}
