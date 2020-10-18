package capstone.backend.api.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table( name = "ReportKeyResults")
public class ReportKeyResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "reportId")
    private Report report;

    @ManyToOne
    @JoinColumn(name = "keyResultId")
    private KeyResult keyResult;

    @NonNull
    private int valueObtained;

    @NonNull
    private int problems;

    @Builder.Default
    private boolean isDelete = false;
}
