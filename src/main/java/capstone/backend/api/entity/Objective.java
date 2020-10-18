package capstone.backend.api.entity;

import lombok.*;

import javax.persistence.*;

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


    private String parentId;

    @NonNull
    private String name;

    //type = 0 objective company, type = 1 objective project, type = 2 objective personal
    @NonNull
    private int type;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "cycleId")
    private Cycle cycle;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "executeId")
    private Execute execute;

    // status = "RUNNING","FINISHED","DRAFT"
    @NonNull
    private String status;

    @NonNull
    private int weight;

    private String history;

    @NonNull
    private String content;

    private int progress;

    private int changing;

    private String alignmentObjectives;

    @Builder.Default
    private boolean isDelete = false;
}
