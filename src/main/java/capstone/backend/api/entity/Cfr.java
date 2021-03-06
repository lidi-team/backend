package capstone.backend.api.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "cfrs")
public class Cfr {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "senderId")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiverId")
    private User receiver;

    @ManyToOne
    @JoinColumn(name = "reportId")
    private Report report;

    @ManyToOne
    @JoinColumn(name = "objectiveId")
    private Objective objective;

    private String content;

    private String type;

    @ManyToOne
    @JoinColumn(name = "evaluationCriteriaId")
    private EvaluationCriteria evaluationCriteria;

    @Builder.Default
    private Date createAt = new Date();

}
