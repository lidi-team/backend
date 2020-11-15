package capstone.backend.api.repository;

import capstone.backend.api.entity.ProjectPosition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectPositionRepository extends JpaRepository<ProjectPosition, Long> {
    Page<ProjectPosition> findAll(Pageable of);

    Page<ProjectPosition> findByIsDeleteFalse(Pageable of);

    ProjectPosition findByIdAndIsDeleteFalse(long id);

}
