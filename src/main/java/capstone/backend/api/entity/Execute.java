package capstone.backend.api.entity;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "executes")
public class Execute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;


    @ManyToOne
    @JoinColumn(name = "projectId")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "positionId")
    private ProjectPosition position;

    @Builder.Default
    private boolean isPm = false;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @NonNull
    private Date fromDate;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @NonNull
    private Date endDate;

    @Builder.Default
    private boolean isDelete = false;
}
