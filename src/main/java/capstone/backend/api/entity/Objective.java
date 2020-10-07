package capstone.backend.api.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "objectives")
public class Objective {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int progress;

    private boolean isRootObjective;

    @NonNull
    private String title;

    @NonNull
    private String content;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cycleId")
    private Cycle cycle;

    @ManyToOne(optional = true, cascade = CascadeType.ALL)
     @JoinColumn(name = "parentObjectiveId")
    private Objective parentObjective;

    @OneToMany(mappedBy = "parentObjective", cascade={CascadeType.ALL})
    private Set<Objective> objectives;


}
