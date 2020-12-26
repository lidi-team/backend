package capstone.backend.api.repository;

import capstone.backend.api.entity.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findAllByObjectiveId(long id);

    Report findFirstByObjectiveIdOrderByIdDesc(long id);

    List<Report> findAllByObjectiveIdAndStatusContains(long id,String status);

    @Query(value = "select r from Report r " +
            "join Objective o on r.objective.id = o.id " +
            "where r.status = :status " +
            "and o.cycle.id= :cycleId " +
            "and o.execute.isDelete = false " +
            "and o.execute.close = false " +
            "and r.authorizedUser.id = :reviewerId " +
            "order by r.checkinDate desc ")
    Page<Report> findByReviewerAndStatusAndCycle(long reviewerId, String status, long cycleId,Pageable pageable);

    Report findFirstByObjectiveId(long id);

    @Query(value = "select r from Report r " +
            "join Objective  o on r.objective.id = o.id " +
            "where o.execute.reviewer.id = :userId " +
            "and r.status = 'Reviewed' " +
            "and r.isLeaderFeedback = false ")
    Page<Report> findAllInferiorRequest(@Param(value = "userId") long userId,
                                        Pageable pageable);

    @Query(value = "select r from Report r " +
            "join Objective  o on r.objective.id = o.id " +
            "where o.execute.user.id = :userId " +
            "and r.status = 'Reviewed' " +
            "and r.isStaffFeedback = false ")
    Page<Report> findAllSuperiorRequest(@Param(value = "userId") long userId,
                                        Pageable pageable);
    @Query(value = "select r from Report r " +
            "join Objective o on r.objective.id = o.id " +
            "where o.execute.user.id = :userId " +
            "and o.cycle.id = :cycleId ")
    List<Report> findAllByUserIdAndCycleId(@Param(value = "userId") long userId,
                                           @Param(value = "cycleId") long cycleId);
}
