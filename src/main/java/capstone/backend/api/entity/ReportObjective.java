package capstone.backend.api.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table( name = "ReportObjectives")
public class ReportObjective {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "reportId")
    private Report report;

    @ManyToOne
    @JoinColumn(name = "objectiveId")
    private Objective objective;

    @NonNull
    private int progress;

    private String problems;
}
