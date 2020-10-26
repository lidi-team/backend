package capstone.backend.api.repository;

import capstone.backend.api.entity.Objective;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ObjectiveRepository extends JpaRepository<Objective, Long> {

    List<Objective> findAllByCycleIdAndParentId(long cycleId, long objectiveId);

    @Query(value = "select o from Objective o where o.execute.user.id = :userId and o.type = 2")
    List<Objective> findAllByUserId(@Param(value = "userId") long userId);

    @Query(value = "select o from Objective o join Execute e on o.execute.id = e.id where e.project.id = :projectId and o.cycle.id = :cycleId and o.type = :type")
    List<Objective> findAllByProjectIdAndCycleIdAndType(@Param(value = "projectId") long projectId,
                                                        @Param(value = "cycleId") long cycleId,
                                                        @Param(value = "type") int type);

    List<Objective> findAllByTypeAndCycleId(int type, long cycle);

}
