package capstone.backend.api.repository;

import capstone.backend.api.entity.Project;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Page<Project> findAllByNameContains(String text,Pageable pageable);

    Page<Project> findAllByCloseAndNameContains(boolean active,String text, Pageable pageable);

    List<Project> findAllByParentId(long id);

    @Query(value = "select p from Project p " +
            "where p.fromDate <= :date and p.endDate > :date ")
    List<Project> findAllByFromDateAndEndDate(Date date);

}
