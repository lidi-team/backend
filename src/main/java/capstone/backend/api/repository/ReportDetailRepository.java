package capstone.backend.api.repository;

import capstone.backend.api.entity.ReportDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportDetailRepository extends JpaRepository<ReportDetail, Long> {

    List<ReportDetail> findAllByReportId(long id);
}
