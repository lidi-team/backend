package capstone.backend.api.repository;

import capstone.backend.api.entity.KeyResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface KeyResultRepository extends JpaRepository<KeyResult, Long> {

    @Modifying
    @Transactional
    @Query(value = "update KeyResult set isDelete = true where objective.id = :id")
    void deleteKeyResultsByObjectiveId(@Param(value = "id") long id);

    @Query(value = "select k from KeyResult k where k.objective.id = :id and k.isDelete = false ")
    ArrayList<KeyResult> findAllByObjectiveId(@Param(value = "id") long id);

    @Modifying
    @Transactional
    @Query(value = "update key_results set parent_id = 0 where id in (" +
            " select c.id from( \n" +
            " select id from key_results where parent_id = :id) " +
            " as c)", nativeQuery = true)
    void updateKeyResultParentId(@Param(value = "id") long id);

    @Modifying
    @Transactional
    @Query(value = "update KeyResult k " +
            "set k.progress = :progress, k.valueObtained = :valueObtain where k.id = :id ")
    void updateKeyResultProgress(@Param(value = "progress") double progress,
                                 @Param(value = "valueObtain") double valueObtain,
                                 @Param(value = "id") long id);

    List<KeyResult> findAllByParentId(long id);
}
