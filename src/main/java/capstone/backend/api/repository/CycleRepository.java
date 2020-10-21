package capstone.backend.api.repository;

import capstone.backend.api.entity.Cycle;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface CycleRepository extends JpaRepository<Cycle, Long> {
    Cycle findFirstByFromDateBeforeAndEndDateAfter(@NonNull Date fromDate, @NonNull Date endDate);
}
