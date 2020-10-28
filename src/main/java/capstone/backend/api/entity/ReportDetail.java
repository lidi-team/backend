package capstone.backend.api.entity;

import lombok.*;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "ReportDetails")
public class ReportDetail {

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
    @NumberFormat(pattern = "#.##")
    private double valueObtained;

    @NumberFormat(pattern = "#.##")
    private double targetValue;

    private double confidentLevel;

    private String progress;

    private String plans;

    private String problems;

    @Builder.Default
    private boolean isDelete = false;
}
