package capstone.backend.api.repository;

import capstone.backend.api.entity.Execute;
import capstone.backend.api.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExecuteRepository extends JpaRepository<Execute, Long> {
    Optional<Execute> findAllByUserId(long id);

    Optional<Execute> findByUserIdAndProjectId(long userId,long projectId);
}