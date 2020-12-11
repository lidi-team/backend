package capstone.backend.api.repository;

import capstone.backend.api.entity.Department;
import capstone.backend.api.entity.EvaluationCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationCriteriaRepository extends JpaRepository<EvaluationCriteria, Long> {
    List<EvaluationCriteria> findEvaluationCriteriaByType(String type);

    Page<EvaluationCriteria> findByIsDeleteFalse(Pageable of);

    EvaluationCriteria findByIdAndIsDeleteFalse(long id);

    Page findByContentContainsAndIsDeleteFalse(String name, Pageable pageable);
}
