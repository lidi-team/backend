package capstone.backend.api.entity;

import lombok.*;
import org.springframework.format.annotation.NumberFormat;

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


    private long parentId;

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
    @NumberFormat(pattern = "#.#")
    private double weight;

    private String history;

    @NonNull
    private String content;

    @NumberFormat(pattern = "#.#")
    private double progress;

    @NumberFormat(pattern = "#.#")
    private double changing;

    private String alignmentObjectives;

    @Builder.Default
    private boolean isDelete = false;
}
