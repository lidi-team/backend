package capstone.backend.api.repository;

import capstone.backend.api.entity.Role;
import capstone.backend.api.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsUserByEmail(String email);

    Page<User> findByRoles(Role role, PageRequest of);

    List<User> findAllByRolesContains(Role role, Pageable pageable);

    List<User> findAllByRolesContains(Role role);

    List<User> findByFullNameContainsAndRoles(String name, Role role, Pageable pageable);

    List<User> findByFullNameContainsAndRoles(String name, Role role);

    List<User> findByFullNameContains(String name, Pageable pageable);

    List<User> findByFullNameContains(String name);

}
