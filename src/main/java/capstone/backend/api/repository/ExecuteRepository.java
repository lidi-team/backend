package capstone.backend.api.repository;

import capstone.backend.api.entity.Execute;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExecuteRepository extends JpaRepository<Execute, Long> {


    @Query(value = "select e from Execute  e where e.user.id = :id " +
            "and ( e.fromDate < :startCycle or e.endDate > :endCycle) ")
    ArrayList<Execute> findExecutesByUserIdAndCycle(long id, Date startCycle,Date endCycle);

    ArrayList<Execute> findAllByUserId(long id);

    Optional<Execute> findByUserIdAndProjectId(long userId, long projectId);

    @Query(value = "select e from Execute e " +
            "join Project p on e.project.id = p.id " +
            "where e.user.id = :id " +
            "and p.close = false " +
            "and e.isDelete = false")
    List<Execute> findAllByUserIdAndOpenProject(@Param(value = "id") long id);

    @Query(value = "select e from Execute e " +
            "join Project p on e.project.id = p.id " +
            "where e.user.id = :userId " +
            "and e.project.id = :projectId " +
            "and p.close = false " +
            "and e.isDelete = false")
    List<Execute> findAllByUserIdAndProjectId(@Param(value = "userId") long userId,
                                              @Param(value = "projectId") long projectId);

    @Query(value = "select e from Execute e " +
            "join Project p on e.project.id = p.id " +
            "where e.project.id = :id " +
            "and e.isDelete = false " +
            "and e.isPm = true")
    Execute findPmByProjectId(@Param(value = "id") long id);

    @Query(value = "select e from Execute e " +
            "join Project p on e.project.id = p.id " +
            "where e.project.id = :id " +
            "and e.isPm = true")
    Execute findPmAllByProjectId(@Param(value = "id") long id);


    @Query(value = "select e from Execute e " +
            "join Project p on e.project.id = p.id " +
            "where e.project.id = :id " +
            "and p.close = false " +
            "and e.isDelete = false " +
            "and e.close = false " +
            "and e.isPm = false")
    List<Execute> findAllStaffByProjectId(@Param(value = "id") long id);

    @Query(value = "select count(e.id) as num from executes e\n" +
            "join projects p on e.project_id = p.id\n" +
            "where p.id <> :projectId and e.user_id = :userId\n" +
            "and p.close = false and e.is_delete = false\n" +
            "and e.is_pm = true", nativeQuery = true)
    int findOtherProjectUserIsPm(@Param(value = "userId") long userId, @Param(value = "projectId") long projectId);

    @Query(value = "select e from Execute e " +
            "where e.reviewer.id = :userId " +
            "and e.user.id <> :userId " +
            "and e.isDelete = false " +
            "and (select count(o.id) from Objective o where o.execute = e and o.cycle.id = :cycleId) > 0 ")
    Page<Execute> findExecuteByReviewerId(@Param(value = "cycleId") long cycleId, @Param(value = "userId") long userId, Pageable pageable);

    @Query(value = "select e from Execute e " +
            "where e.reviewer.id = :userId " +
            "and e.user.id <> :userId " +
            "and e.project.id = :projectId " +
            "and e.isDelete = false " +
            "and (select count(o.id) from Objective o where o.execute = e and o.cycle.id = :cycleId) > 0 ")
    Page<Execute> findExecuteByReviewerIdAndProjectId(@Param(value = "cycleId") long cycleId,
                                                      @Param(value = "userId") long userId,
                                                      @Param(value = "projectId") long projectId,
                                                      Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "update Execute e set e.isDelete = true, e.close = true where e.user.id = :userId and e.project.id = :projectId")
    void removeStaff(long projectId, long userId);

    @Query(value = "select e.user.id from Execute e " +
            "where e.user.id = :id " +
            "and e.isPm = true " +
            "and e.isDelete = false ")
    List<Long> findAllProjectIdByUserId(long id);

    Execute findFirstByProjectIsNull();

    Execute findByProjectIdAndUserId(long projectId, long userId);

    @Query(value = "select e.id from Execute e where e.project.id = :id")
    List<Long> findAllByProjectId(@Param(value = "id") long id);

    @Transactional
    @Modifying
    @Query(value = "update Execute e set e.close = true where e.project.id = :id")
    void updateAllStatusWhenCloseProject(@Param(value = "id") long id);


}
