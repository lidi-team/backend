package capstone.backend.api.entity;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

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
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Column(name = "dateOfBirth")
    private Date dob;

    @NonNull
    private String fullName;

    // gender = 1 male; gender = 2 female
    @NonNull
    private int gender;

    private String avatarImage;


    @ManyToOne
    @JoinColumn(name = "departmentId")
    private Department department;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date deactiveAt;

    @Builder.Default
    private boolean isActive = true;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date createAt;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Builder.Default
    private Date updateAt = new Date();

    private int star;

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