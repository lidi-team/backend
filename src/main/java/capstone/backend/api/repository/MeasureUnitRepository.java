package capstone.backend.api.repository;

import capstone.backend.api.entity.MeasureUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeasureUnitRepository extends JpaRepository< MeasureUnit, Long> {

}
