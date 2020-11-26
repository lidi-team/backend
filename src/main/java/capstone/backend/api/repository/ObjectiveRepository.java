package capstone.backend.api.repository;

import capstone.backend.api.entity.Objective;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ObjectiveRepository extends JpaRepository<Objective, Long> {

    @Query(value = "select o from Objective o " +
            "where o.cycle.id = :cycleId " +
            "and o.parentId = :objectiveId " +
            "and o.isDelete = false")
    List<Objective> findAllByCycleIdAndParentId(@Param(value = "cycleId") long cycleId,@Param(value = "objectiveId") long objectiveId);

    @Query(value = "select o from Objective o " +
            "where o.execute.user.id = :userId " +
            "and o.type = 2 " +
            "and o.isDelete = false")
    List<Objective> findAllByUserId(@Param(value = "userId") long userId);

    @Query(value = "select o from Objective o " +
            "join Execute e on o.execute.id = e.id " +
            "where e.project.id = :projectId " +
            "and o.cycle.id = :cycleId " +
            "and o.type = :type " +
            "and o.isDelete = false")
    List<Objective> findAllByProjectIdAndCycleIdAndType(@Param(value = "projectId") long projectId,
                                                        @Param(value = "cycleId") long cycleId,
                                                        @Param(value = "type") int type);

    @Query(value = "select o from Objective o " +
            "where o.type = :type " +
            "and o.cycle.id = :cycleId " +
            "and o.execute.user.id = :userId " +
            "and o.isDelete = false")
    Page<Objective> findAllByTypeAndCycleIdAndUserId(@Param(value = "type")int type,
                                                     @Param(value = "cycleId") long cycleId,
                                                     @Param(value = "userId") long userId,
                                                     Pageable pageable);

    @Query(value = "select o from Objective o " +
            "where o.type = :type " +
            "and o.cycle.id = :cycleId " +
            "and o.execute.user.id = :userId " +
            "and o.execute.project.id =:projectId " +
            "and o.isDelete = false")
    Page<Objective> findAllByTypeAndCycleIdAndUserIdAndProjectId(@Param(value = "type")int type,
                                                     @Param(value = "cycleId") long cycleId,
                                                     @Param(value = "userId") long userId,
                                                     @Param(value = "projectId") long projectId,
                                                     Pageable pageable);

    @Query(value = "select o from Objective o " +
            "join Execute e on o.execute.id = e.id " +
            "where e.user.id = :userId " +
            "and o.parentId = :objectiveId " +
            "and o.type = :type " +
            "and o.isDelete = false")
    List<Objective> findAllByParentIdAndUserIdAndType(@Param(value = "objectiveId") long objectiveId,
                                                        @Param(value = "userId") long userId,
                                                        @Param(value = "type") int type);

    @Query(value = "select o from Objective o " +
            "where o.parentId = :objectiveId " +
            "and o.type = :type " +
            "and o.isDelete = false ")
    List<Objective> findAllByParentIdAndType(@Param(value = "objectiveId") long objectiveId,
                                             @Param(value = "type") int type);

    @Modifying
    @Transactional
    @Query("update Objective o set o.isDelete = true where o.id = :id")
    void deleteObjective(@Param(value = "id") long id);

    @Modifying
    @Transactional
    @Query("update Objective o set o.changing = :changing , o.progress = :progress where o.id = :id")
    void updateChangingAndProgressObjective(@Param(value="changing")double changing,
                                            @Param(value = "progress") double progress,
                                            @Param(value = "id") long id);

    Objective findFirstByParentId(long parentId);

    @Query(value = "select o from Objective o where o.id = :id and o.isDelete = false")
    Objective findByIdAndDelete(long id);

    @Query(value = "select o from Objective o " +
            "where o.parentId = :objectiveId " +
            "and o.isDelete = false")
    List<Objective> findAllByParentId(@Param(value = "objectiveId") long objectiveId);

    @Query(value = "select o from Objective o " +
            " join Execute e on o.execute.id = e.id" +
            " where e.project.id = :projectId" +
            " and o.cycle.id = :cycleId" +
            " and o.isDelete = false")
    List<Objective> findAllObjectiveByProjectIdAndCycleId(@Param(value = "projectId") long projectId,
                                                        @Param(value = "cycleId") long cycleId);

    @Query(value = "select o from Objective o " +
            " join Execute e on o.execute.id = e.id" +
            " where e.project.id = :projectId" +
            " and o.cycle.id = :cycleId" +
            " and o.isDelete = false" +
            " and o.type = 1")
    List<Objective> findAllProjectObjective(@Param(value = "projectId") long projectId,
                                            @Param(value = "cycleId") long cycleId);

    @Query(value = "select o from Objective o " +
            " where o.cycle.id = :cycleId" +
            " and o.isDelete = false" +
            " and o.type = 0")
    List<Objective> findCompanyObjective(@Param(value = "cycleId") long cycleId);

}
