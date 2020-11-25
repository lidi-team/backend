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
@Table(name = "Reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "authorizedUserId")
    private User authorizedUser;

    @NonNull
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date nextCheckinDate;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date checkinDate;

    @ManyToOne
    @JoinColumn(name = "objectiveId")
    private Objective objective;

    // "Draft","Pending","Reviewed"
    @NonNull
    private String status;

    private double progress;

    @Builder.Default
    private boolean isLeaderFeedback = false;

    @Builder.Default
    private boolean isStaffFeedback = false;

    @Builder.Default
    private boolean isDelete = false;
}
