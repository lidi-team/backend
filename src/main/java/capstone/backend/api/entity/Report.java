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

    @NonNull
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date checkinDate;

    @ManyToOne
    @JoinColumn(name = "objectiveId")
    private Objective objective;

    // "DRAFT","SUBMITTED","REJECTED","APPROVED"
    @NonNull
    private String status;

    private boolean isLeaderFeedback;

    private boolean isStaffFeedback;

    @Builder.Default
    private boolean isDelete = false;
}
