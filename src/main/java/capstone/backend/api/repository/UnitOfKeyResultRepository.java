package capstone.backend.api.repository;

import capstone.backend.api.entity.Department;
import capstone.backend.api.entity.UnitOfKeyResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnitOfKeyResultRepository extends JpaRepository<UnitOfKeyResult, Long> {
    Page findByNameContainsAndIsDeleteFalse(String name, Pageable pageable);

    Page<UnitOfKeyResult> findByIsDeleteFalse(Pageable of);

    UnitOfKeyResult findByIdAndIsDeleteFalse(long id);
}
