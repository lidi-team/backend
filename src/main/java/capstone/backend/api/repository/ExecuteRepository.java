package capstone.backend.api.repository;

import capstone.backend.api.entity.Execute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExecuteRepository extends JpaRepository<Execute, Long> {
    ArrayList<Execute> findAllByUserId(long id);

    Optional<Execute> findByUserIdAndProjectId(long userId, long projectId);

    @Query(value =  "select e from Execute e " +
                    "join Project p on e.project.id = p.id " +
                    "where e.user.id = :id and p.isClose = false and e.isDelete = false")
    List<Execute> findAllByUserIdAndOpenProject(@Param(value = "id") long id);
}
