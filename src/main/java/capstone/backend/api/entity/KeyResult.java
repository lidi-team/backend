package capstone.backend.api.entity;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

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
    private int fromValue;

    @NonNull
    private int toValue;

    private int valueObtained;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @NonNull
    private Date deadline;

    @NonNull
    private String content;

    private String reference;

    private int weight;

    @Builder.Default
    private boolean isDelete = false;

}
