package capstone.backend.api.repository;

import capstone.backend.api.entity.Project;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findAllByNameContains(String text);

    List<Project> findAllByCloseAndNameContains(boolean active,String text);

    List<Project> findAllByParentId(long id);

    @Query(value = "select p from Project p " +
            "where p.fromDate <= :date and p.endDate > :date ")
    List<Project> findAllByFromDateAndEndDate(Date date);

    @Query(value = "select p from Project p " +
            "where (p.parent.id = 0  or p.parent is null) " +
            "and p.close = false " +
            "and p.isDelete = false ")
    List<Project> findAllParentProject();

    Page<Project> findAllByNameContainsAndIdIn(String text,List<Long> ids,Pageable pageable);

    Page<Project> findAllByCloseAndNameContainsAndIdIn(boolean active,String text,List<Long> ids, Pageable pageable);

    @Query(value = "select p.id from Project p " +
            "join Execute e on p.id = e.project.id " +
            "where e.user.id = :userId and e.isDelete = false")
    List<Long> findAllProjectOfUser(@Param(value = "userId") long userId);

}
