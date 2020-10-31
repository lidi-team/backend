package capstone.backend.api.repository;

import capstone.backend.api.entity.KeyResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Repository
public interface KeyResultRepository extends JpaRepository<KeyResult, Long> {

    @Transactional
    void deleteKeyResultsByObjectiveId(long id);

    ArrayList<KeyResult> findAllByObjectiveId(long id);

    @Transactional
    @Query(value = "update KeyResult k set k.parentId = -1 where k.parentId = :id")
    void updateKeyResultParentId(@Param(value = "id") long id);
}
