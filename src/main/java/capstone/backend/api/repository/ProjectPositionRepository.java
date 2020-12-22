package capstone.backend.api.repository;

import capstone.backend.api.entity.ProjectPosition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectPositionRepository extends JpaRepository<ProjectPosition, Long> {
    Page findByNameContainsAndIsDeleteFalse(String name, Pageable pageable);

    Page<ProjectPosition> findAll(Pageable of);

    Page<ProjectPosition> findByIsDeleteFalse(Pageable of);

    ProjectPosition findByIdAndIsDeleteFalse(long id);

    @Query(value = "select e.id from Execute e where e.position.id = :id ")
    List<Long> checkExisted(@Param(value = "id") long id);

    @Query(value = "select p from ProjectPosition p where p.isDelete = false ")
    List<ProjectPosition> findAllDeleteFalse();

}
