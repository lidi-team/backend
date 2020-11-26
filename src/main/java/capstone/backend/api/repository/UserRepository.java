package capstone.backend.api.repository;

import capstone.backend.api.entity.ApiResponse.MetaDataResponse;
import capstone.backend.api.entity.Role;
import capstone.backend.api.entity.User;
import org.hibernate.annotations.NamedNativeQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
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

    @Query(value = "select u from User u where lower(u.fullName) like %:name% and u.isDelete = false and u.isActive = true ")
    List<User> findByFullNameContains(@Param(value = "name") String name);


}
