package capstone.backend.api.repository;

import capstone.backend.api.entity.Execute;
import capstone.backend.api.entity.Objective;
import capstone.backend.api.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
            "and p.close = false " +
            "and e.isDelete = false " +
            "and e.isPm = false")
    List<Execute> findAllStaffByProjectId(@Param(value = "id") long id);

    @Query(value = "select count(e.id) as num from executes e\n" +
            "join projects p on e.project_id = p.id\n" +
            "where p.id <> :projectId and e.user_id = :userId\n" +
            "and p.close = false and e.is_delete = false\n" +
            "and e.is_pm = true",nativeQuery = true)
    int findOtherProjectUserIsPm(@Param(value = "userId") long userId,@Param(value = "projectId")long projectId);

    @Query(value = "select e from Execute e " +
            "where e.reviewer.id = :userId " +
            "and e.user.id <> :userId ")
    Page<Execute> findExecuteByReviewerId(@Param(value = "userId") long userId, Pageable pageable);

    @Query(value = "select e from Execute e " +
            "where e.reviewer.id = :userId " +
            "and e.user.id <> :userId " +
            "and e.project.id = :projectId ")
    Page<Execute> findExecuteByReviewerIdAndProjectId(@Param(value = "userId") long userId,
                                                @Param(value = "projectId") long projectId,
                                                Pageable pageable);
}
