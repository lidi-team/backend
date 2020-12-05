package capstone.backend.api.repository;

import capstone.backend.api.entity.Cfr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CfrRepository extends JpaRepository<Cfr, Long> {

    @Query("select c from Cfr c " +
            " join Objective o on c.objective.id = o.id " +
            "where o.cycle.id = :cycleId")
    List<Cfr> findAllByCycleId(@Param(value = "cycleId") long cycleId);

    @Query("select c from Cfr c " +
            "join Report r on r.id = c.report.id " +
            "join Objective o on r.objective.id = o.id " +
            "where o.cycle.id = :cycleId")
    List<Cfr> findAllByCycleId2(@Param(value = "cycleId") long cycleId);
}
