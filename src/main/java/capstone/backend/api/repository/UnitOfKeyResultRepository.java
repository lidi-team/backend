package capstone.backend.api.repository;

import capstone.backend.api.entity.UnitOfKeyResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnitOfKeyResultRepository extends JpaRepository<UnitOfKeyResult,Long> {
}
