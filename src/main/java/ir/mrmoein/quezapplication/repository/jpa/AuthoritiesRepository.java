package ir.mrmoein.quezapplication.repository.jpa;

import ir.mrmoein.quezapplication.model.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthoritiesRepository extends JpaRepository<Authority, Long> {
}
