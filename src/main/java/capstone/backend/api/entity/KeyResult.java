package capstone.backend.api.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "keyResults")
public class KeyResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    private int startValue;
    @NonNull
    private int valueObtained;
    @NonNull
    private int targetedValue;

    @NonNull
    private String content;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "objectiveId")
    private Objective objective;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "measureUnitId")
    private MeasureUnit measureUnit;

    private String common;
}
