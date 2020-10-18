package capstone.backend.api.repository;

import capstone.backend.api.entity.ReportKeyResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportKeyResultRepository extends JpaRepository<ReportKeyResult,Long> {
}
