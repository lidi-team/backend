package capstone.backend.api.repository;

import capstone.backend.api.entity.ProjectPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectPositionRepository extends JpaRepository<ProjectPosition, Long> {
}
