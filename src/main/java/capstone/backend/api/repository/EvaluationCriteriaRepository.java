package capstone.backend.api.repository;

import capstone.backend.api.entity.Department;
import capstone.backend.api.entity.EvaluationCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationCriteriaRepository extends JpaRepository<EvaluationCriteria, Long> {
    List<EvaluationCriteria> findEvaluationCriteriaByType(String type);

    Page<EvaluationCriteria> findByIsDeleteFalse(Pageable of);

    EvaluationCriteria findByIdAndIsDeleteFalse(long id);

    @Query(value = "select e from EvaluationCriteria e where e.isDelete = false ")
    List<EvaluationCriteria> findAllAndDeleteFalse();

    Page findByContentContainsAndIsDeleteFalse(String name, Pageable pageable);

    @Query(value = "select e from EvaluationCriteria  e where e.content = :name and e.isDelete = false ")
    EvaluationCriteria findByContentAndDeleteFalse(@Param(value = "name") String name);

    @Query(value = "select c.id from Cfr c where c.evaluationCriteria.id = :id")
    List<Long> checkExist(@Param(value = "id") long id);
}
