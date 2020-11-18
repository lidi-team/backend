package capstone.backend.api.repository;

import capstone.backend.api.entity.Cycle;
import capstone.backend.api.entity.Department;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface CycleRepository extends JpaRepository<Cycle, Long> {
    Page<Cycle> findAllByFromDateLessThanEqualAndEndDateGreaterThanEqual(Date fromeDate, Date endDate, Pageable pageable);

    Cycle findFirstByFromDateBeforeAndEndDateAfter(@NonNull Date fromDate, @NonNull Date endDate);

    Page<Cycle> findAllByNameContains(String text, Pageable pageable);

    Page<Cycle> findByIsDeleteFalse(Pageable of);

    Page<Cycle> findAllByNameContainsAndIsDeleteFalse(String text, Pageable pageable);

    Cycle findByIdAndIsDeleteFalse(long id);

}
