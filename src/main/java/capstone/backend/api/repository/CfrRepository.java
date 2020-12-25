package capstone.backend.api.repository;

import capstone.backend.api.entity.Cfr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CfrRepository extends JpaRepository<Cfr, Long> {

    @Query("select c from Cfr c where c.createAt >= :fromDate and c.createAt <= :toDate")
    List<Cfr> findAllByCycleId(@Param(value = "fromDate") Date fromDate,
                               @Param(value = "toDate") Date toDate);

    List<Cfr> findAllByReceiverIdAndTypeIn(long id,List<String> types);

    List<Cfr> findAllBySenderIdAndTypeIn(long id,List<String> types);
}
