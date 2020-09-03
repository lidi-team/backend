package capstone.backend.api.repository;

import capstone.backend.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsUserByEmail(String email);

    boolean existsUserByEmailAndPassword(String email, String password);

    @Query("update User u set u.password = :password where u.email = :email")
    void updateUserPassword(@Param("password") String password,@Param("email") String email);

}
