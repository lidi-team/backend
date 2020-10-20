package capstone.backend.api.repository;

import capstone.backend.api.entity.Objective;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ObjectiveRepository extends JpaRepository<Objective,Long> {

    List<Objective> findAllByCycleIdAndParentId(long cycleId,long objectiveId);

    @Query(value = "select o from Objective o where o.execute.user.id = :userId and o.type = 2")
    List<Objective> findAllByUserId(@Param(value = "userId") long userId);
}
