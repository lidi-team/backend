package capstone.backend.api.repository;

import capstone.backend.api.entity.KeyResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Repository
public interface KeyResultRepository extends JpaRepository<KeyResult,Long> {

    @Transactional
    void deleteKeyResultsByObjectiveId(long id);

    @Transactional
    void deleteKeyResultsById(long id);

    ArrayList<KeyResult> getKeyResultsByObjectiveId(long id);

}
