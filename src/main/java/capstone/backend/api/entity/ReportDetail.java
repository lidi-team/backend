package capstone.backend.api.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table( name = "ReportDetails")
public class ReportDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "keyResultId")
    private KeyResult keyResult;

    @NonNull
    private int valueObtained;

    @NonNull
    private int confidentLevel;

    @NonNull
    private int problems;

    @Builder.Default
    private boolean isDelete = false;
}
