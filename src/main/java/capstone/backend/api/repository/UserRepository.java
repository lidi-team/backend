package capstone.backend.api.repository;

import capstone.backend.api.entity.Role;
import capstone.backend.api.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsUserByEmail(String email);

    Page<User> findAllByDepartmentId(long id, PageRequest of);

    Page<User> findByRoles(Role role, PageRequest of);

    List<User> findAllByRolesContains(Role role);

}
