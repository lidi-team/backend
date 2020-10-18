package capstone.backend.api.repository;

import capstone.backend.api.entity.Objective;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ObjectiveRepository extends JpaRepository<Objective,Long> {

    List<Objective> findAllByCycleIdAndParentIdContains(long cycleId,String id);
}
