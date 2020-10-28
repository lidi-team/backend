package capstone.backend.api.repository;

import capstone.backend.api.entity.Cfr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CfrRepository extends JpaRepository<Cfr, Long> {
}
