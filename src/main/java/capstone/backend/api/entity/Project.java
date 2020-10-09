package capstone.backend.api.entity;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
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
    private Project parentProject;

    @NonNull
    private String name;

    @NonNull
    private String status;

    private boolean isPm;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @NonNull
    private Date fromDate;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @NonNull
    private Date endDate;

    @OneToMany
    @Builder.Default
    private Set<Project> projects = new HashSet<>();

    @Builder.Default
    private boolean isDelete = false;
}
