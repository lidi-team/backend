package capstone.backend.api.repository;

import capstone.backend.api.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findAllByObjectiveId(long id);

    Report findByObjectiveId(long id);


    Report findFirstByObjectiveIdOrderByIdDesc(long id);

}
