package capstone.backend.api.repository;

import capstone.backend.api.entity.Objective;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ObjectiveRepository extends JpaRepository<Objective, Long> {

    List<Objective> findAllByCycleIdAndParentIdAndDeleteFalse(long cycleId, long objectiveId);

    @Query(value = "select o from Objective o where o.execute.user.id = :userId and o.type = 2 and o.isDelete = false")
    List<Objective> findAllByUserId(@Param(value = "userId") long userId);

    @Query(value = "select o from Objective o join Execute e on o.execute.id = e.id where e.project.id = :projectId and o.cycle.id = :cycleId and o.type = :type and o.isDelete = false")
    List<Objective> findAllByProjectIdAndCycleIdAndType(@Param(value = "projectId") long projectId,
                                                        @Param(value = "cycleId") long cycleId,
                                                        @Param(value = "type") int type);

    List<Objective> findAllByTypeAndCycleIdAndDeleteFalse(int type, long cycle);

    @Transactional
    @Query(value = "update Objective o set o.parentId = -1 where o.parentId = :id")
    void updateObjectiveParentId(@Param(value = "id") long id);

    @Query("update Objective o set o.isDelete = true where o.id = :id")
    void deleteObjective(@Param(value = "id") long id);
}
