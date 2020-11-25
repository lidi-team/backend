package capstone.backend.api.entity;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Setter
@Getter
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

    private String description;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @NonNull
    private Date fromDate;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @NonNull
    private Date endDate;

    @Builder.Default
    private boolean isDelete = false;
}
