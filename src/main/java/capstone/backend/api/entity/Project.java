package capstone.backend.api.entity;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "parentId")
    private Project parent;

    @NonNull
    private String name;

    @NonNull
    @Builder.Default
    private boolean close = false;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @NonNull
    private Date fromDate;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @NonNull
    private Date endDate;

    private String description;

    private int weight;

    @OneToMany
    private Set<Project> childProject = new HashSet<>();

    @Builder.Default
    private boolean isDelete = false;
}
