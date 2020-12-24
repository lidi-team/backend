package capstone.backend.api.repository;

import capstone.backend.api.entity.Department;
import capstone.backend.api.entity.UnitOfKeyResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnitOfKeyResultRepository extends JpaRepository<UnitOfKeyResult, Long> {
    Page findByNameContainsAndIsDeleteFalse(String name, Pageable pageable);

    Page<UnitOfKeyResult> findByIsDeleteFalse(Pageable of);

    UnitOfKeyResult findByIdAndIsDeleteFalse(long id);

    UnitOfKeyResult findByName(String name);

    @Query(value = "select k.unitOfKeyResult.id from KeyResult k " +
            "where k.unitOfKeyResult.id = :id ")
    List<Long> checkExisted(long id);
}
