package capstone.backend.api.repository;

import capstone.backend.api.entity.Project;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Page<Project> findAllByNameContains(String text,Pageable pageable);

    Page<Project> findAllByCloseAndNameContains(boolean active,String text, Pageable pageable);
}
