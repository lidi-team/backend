package capstone.backend.api.repository;

import capstone.backend.api.entity.ReportObjective;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportObjectiveRepository extends JpaRepository<ReportObjective,Long> {
}
