package capstone.backend.api.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "cycles")
public class Cycle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    private String name;

    @NonNull
    private Date startDate;

    @NonNull
    private Date endDate;

}
