package capstone.backend.api.repository;

import capstone.backend.api.entity.Department;
import capstone.backend.api.entity.ProjectPosition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Page<Department> findByIsDeleteFalse(Pageable of);

    @Query(value = "select d from Department d where d.isDelete = false")
    List<Department> findAllByDeleteFalse();

    Department findByIdAndIsDeleteFalse(long id);

    Page findByNameContainsAndIsDeleteFalse(String name, Pageable pageable);

    @Query(value = "select e.id from User e where e.department.id = :id ")
    List<Long> checkExisted(long id);

    Department findByName(String name);
}
