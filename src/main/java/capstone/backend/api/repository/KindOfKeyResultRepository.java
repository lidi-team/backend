package capstone.backend.api.repository;

import capstone.backend.api.entity.KindOfKeyResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KindOfKeyResultRepository extends JpaRepository<KindOfKeyResult,Long> {
}
