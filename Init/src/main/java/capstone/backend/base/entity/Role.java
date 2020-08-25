package capstone.backend.base.entity;

import lombok.*;
import org.eclipse.xtend.lib.annotations.Data;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Column(name = "name")
    private String name;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
    private Set<User> users;

    public Role(String name){
        this.name = name;
    }
}
