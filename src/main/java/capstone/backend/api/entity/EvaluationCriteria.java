package capstone.backend.api.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "evaluationCriterias")
public class EvaluationCriteria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String content;

    // "LEADER_TO_STAFF","STAFF_TO_LEADER"
    @NonNull
    private String type;

    private int numberOfStar;

    @Builder.Default
    private boolean isDelete = false;
}
