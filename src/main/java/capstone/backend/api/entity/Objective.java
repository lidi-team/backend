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

    @NonNull
    private int type;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "cycleId")
    private Cycle cycle;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "projectId")
    private Project project;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @NonNull
    private String status;

    @NonNull
    private int weight;

    private String history;

    @NonNull
    private String content;

    @Builder.Default
    private boolean isDelete = false;
}
