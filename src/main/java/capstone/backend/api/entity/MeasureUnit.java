package capstone.backend.api.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "measureUnits")
public class MeasureUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    private String preset;

    @NonNull
    private String type;

    @NonNull
    private int measureIndex;

}
