package capstone.backend.base.entity;

import lombok.*;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username")
})
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NonNull
    private int age;

    @NonNull
    @Column(name = "full_name")
    private String fullName;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(
                    name = "user_id",
                    referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id",
                    referencedColumnName = "id")
    )
    private Set<Role> roles = new HashSet<>();

    public User(String username, String password, int age, String fullName, Set<Role> roles){
        this.username = username;
        this.password = password;
        this.age = age;
        this.fullName = fullName;
        this.roles = roles;
    }
}
