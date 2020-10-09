package capstone.backend.api.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Builder
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    @Email
    private String email;

    @NonNull
    private String password;

    @NonNull
    @Column(name = "dateOfBirth")
    private Date dob;

    @NonNull
    private String fullName;

    @NonNull
    private int gender;

    private String avatarImage;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "departmentId")
    private Department department;

    @NonNull
    @Builder.Default
    private boolean isActive = true;

    private Date deactiveAt;

    private int status;

    private Date createAt;

    private Date updateAt;

    private int point;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "userRoles",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "roleId")
    )
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    @Builder.Default
    private boolean isDelete = false;
}