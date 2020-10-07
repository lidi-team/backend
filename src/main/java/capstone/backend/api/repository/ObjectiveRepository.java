package capstone.backend.api.repository;

import capstone.backend.api.entity.Objective;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ObjectiveRepository extends JpaRepository<Objective,Long> {

    List<Objective> findAllByUserId(long id);

    List<Objective> findAllByCycleIdAndUserId(long cycleId, long userId);

    List<Objective> findAllByCycleId(long id);
}
