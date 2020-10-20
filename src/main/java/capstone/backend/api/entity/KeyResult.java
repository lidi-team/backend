package capstone.backend.api.entity;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import java.util.Date;

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
    @ManyToOne
    @JoinColumn(name = "objectiveId")
    private Objective objective;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "unitId")
    private UnitOfKeyResult unitOfKeyResult;

    private long parentId;

    @NonNull
    @NumberFormat(pattern = "#.##")
    private double fromValue;

    @NonNull
    @NumberFormat(pattern = "#.##")
    private double toValue;

    @NumberFormat(pattern = "#.##")
    private double valueObtained;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date deadline;

    @NonNull
    private String content;

    private String reference;

    @NumberFormat(pattern = "#.##")
    private double weight;

    @Builder.Default
    private boolean isDelete = false;

}
